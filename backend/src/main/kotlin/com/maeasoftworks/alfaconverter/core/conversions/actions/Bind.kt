package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.conversions.Path
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("bind")
class Bind(
	private val initialColumn: Path,
	private val targetColumn: Path
) : Action() {

	override fun run(initialTable: Table, resultTable: Table) {
		for (cell in initialTable[initialColumn]?.cells?.values!!) {
			resultTable[targetColumn]?.cells?.put(cell.row, Cell(targetColumn, cell.row).also { it.value = cell.value })
		}
	}

	override fun isUsing(column: Path) = initialColumn == column
}