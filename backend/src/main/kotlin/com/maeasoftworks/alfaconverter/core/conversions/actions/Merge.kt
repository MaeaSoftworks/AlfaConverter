package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("merge")
internal class Merge(
	@SerialName("initial-columns")
	private val initialColumns: List<Int>,
	@SerialName("target-column")
	private val targetColumn: Int,
	@SerialName("pattern")
	private val pattern: String
) : Action() {

	override fun run(initialTable: Table, resultTable: Table): Table {
		val initialColumns = initialTable[initialColumns]
		for (y in 1..initialTable.rowsCount) {
			var result = pattern
			Table.slice(initialColumns, y).forEach { cell ->
				result = result.replace("$${cell?.column}", cell?.value!!.getString())
			}
			resultTable[targetColumn, y] = Cell(targetColumn, y).also { z ->
				z.value = SString(result)
				z.column = targetColumn
				z.row = y
			}
		}
		return resultTable
	}

	override fun isUsing(column: Int) = initialColumns.any { it == column } || targetColumn == column
}