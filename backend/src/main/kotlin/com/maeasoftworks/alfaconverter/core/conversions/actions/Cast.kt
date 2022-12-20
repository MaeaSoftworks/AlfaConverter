package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xlsx.structure.Factory
import com.maeasoftworks.alfaconverter.core.xlsx.structure.TypeName
import com.maeasoftworks.alfaconverter.core.xlsx.structure.NumberData
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.maeasoftworks.alfaconverter.core.model.Result

/**
 * Action that cast all data from in [targetColumn] to [targetType] with specified [dataFormat].
 *
 * Applicable only for [Xlsx] result implementation.
 *
 * Note: [dataFormat] applicable only for [NumberData].
 * @param targetColumn address of column in [Result] table that will be cast.
 * @param targetType type from [TypeName] enum to which the cast will be performed.
 * @param dataFormat [NumberData] format.
 * @constructor Creates new instance.
 */
@Serializable
@SerialName("cast")
class Cast(
	private val targetColumn: ColumnAddress,
	private val targetType: TypeName,
	private val dataFormat: Long? = 0
) : Action() {
	override fun run(initialTable: Table, resultTable: Table) {
		val cells = resultTable[targetColumn]!!.cells
		for (cell in cells.indices) {
			cells[cell] = Factory.create(cells[cell], targetType, dataFormat)
		}
	}
}