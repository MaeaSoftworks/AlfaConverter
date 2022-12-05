package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.conversions.Path
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.Serializable

@Serializable
class Merge(
	private val initialColumns: List<Path>,
	private val targetColumn: Path,
	private val pattern: String
) : Action() {

	override fun run(initialTable: Table, resultTable: Table) {
		val sourceColumns = initialTable[initialColumns]
		for (y in 1..initialTable.rowsCount) {
			var result = pattern
			Table.slice(sourceColumns, y).forEach { cell ->
				result = result.replace("$${cell?.column?.toString()}", cell?.value!!.getString())
			}
			resultTable[targetColumn, y] = Cell(targetColumn, y).also { z ->
				z.value = SString(result)
				z.column = targetColumn
				z.row = y
			}
		}
	}

	override fun isUsing(column: Path) = initialColumns.any { it == column } || targetColumn == column
}