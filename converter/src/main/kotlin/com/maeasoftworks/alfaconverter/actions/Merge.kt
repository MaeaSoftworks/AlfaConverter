package com.maeasoftworks.alfaconverter.actions

import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.DataFormat
import com.maeasoftworks.alfaconverter.wrappers.Table
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
	private val pattern: String,
	@SerialName("target-data-format")
	private val targetDataFormat: DataFormat = DataFormat.STRING
) : Action() {

	override fun run(initialTable: Table, resultTable: Table): Table {
		val initialColumns = initialTable[initialColumns]
		for (y in 1 until initialTable.rowsCount) {
			resultTable[targetColumn, y] = let {
				var result = pattern
				Table.slice(initialColumns, y).forEach { cell ->
					result = result.replace("$${cell?.column}", cell?.value!!.toString())
				}
				return@let Cell(targetColumn, y).also { z ->
					z.value = result
					z.stringValue = result
					z.column = targetColumn
					z.row = y
					z.format = targetDataFormat
				}
			}
		}
		return resultTable
	}

	override fun uses(column: Int) = initialColumns.any { it == column }
}