package com.maeasoftworks.alfaconverter.actions

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
			resultTable[targetColumn]?.cells?.put(cell.row, cell.also { it.column = targetColumn })
		}
		return resultTable
	}

	override fun uses(column: Int) = initialColumn == column
}