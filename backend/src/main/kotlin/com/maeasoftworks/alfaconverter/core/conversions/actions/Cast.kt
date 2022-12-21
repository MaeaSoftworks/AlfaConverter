package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.maeasoftworks.alfaconverter.core.model.Result
import com.maeasoftworks.alfaconverter.core.xlsx.structure.*

/**
 * Action that cast all data from in [targetColumn] to specified [dataFormat] of [NumberData].
 *
 * Applicable only for [Xlsx] result implementation.
 * @param targetColumn address of column in [Result] table that will be cast.
 * @param dataFormat [NumberData] format.
 * @constructor Creates new instance.
 */
@Suppress("unused")
@Serializable
@SerialName("cast")
class Cast(
	private val targetColumn: ColumnAddress,
	private val dataFormat: Long? = 0
) : Action() {
	override fun run(initialTable: Table, resultTable: Table) {
		val cells = resultTable[targetColumn]!!.cells
		for (cell in cells.indices) {
			cells[cell] = NumberData(cells[cell].getString().toDouble(), dataFormat!!)
		}
	}
}