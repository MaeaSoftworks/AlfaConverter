package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xlsx.structure.Factory
import com.maeasoftworks.alfaconverter.core.xlsx.structure.TypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("cast")
class Cast(
	private val targetColumn: String,
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