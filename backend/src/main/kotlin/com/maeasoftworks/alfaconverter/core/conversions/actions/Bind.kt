package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.conversions.Target
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.Serializable

@Serializable
class Bind(
	private val initialColumn: Target,
	private val targetColumn: Target
) : Action() {

	override fun run(initialTable: Table, resultTable: Table) {
		for (cell in initialTable[initialColumn]?.cells?.values!!) {
			resultTable[targetColumn]?.cells?.put(cell.row, Cell(targetColumn, cell.row).also { it.value = cell.value })
		}
	}

	override fun isUsing(column: Target) = initialColumn == column
}