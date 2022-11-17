package com.maeasoftworks.alfaconverter.conversions.actions

import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("bind")
internal class Bind(
	@SerialName("initial-column")
	val initialColumn: Int,
	@SerialName("target-column")
	val targetColumn: Int
) : Action() {

	override fun run(initialTable: Table, resultTable: Table): Table {
		for (cell in initialTable[initialColumn]?.cells?.values!!) {
			resultTable[targetColumn]?.cells?.put(cell.row, Cell(cell.row, targetColumn).also {
				it.value = cell.value
			})
		}
		return resultTable
	}

	override fun isUsing(column: Int) = initialColumn == column
}