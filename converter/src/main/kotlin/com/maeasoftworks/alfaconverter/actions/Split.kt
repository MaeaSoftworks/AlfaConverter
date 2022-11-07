package com.maeasoftworks.alfaconverter.actions

import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("split")
internal class Split(
	@SerialName("initial-column")
	val initialColumn: Int,
	@SerialName("target-columns")
	val targetColumns: List<Int>,
	val pattern: String
) : Action() {
	override fun run(initialTable: Table, resultTable: Table): Table {
		val initialColumn = initialTable[initialColumn]
		for (y in 1..initialTable.rowsCount) {
			val results = Regex(pattern).matchEntire(initialColumn?.get(y)?.value!!.toString())
			for (col in targetColumns.indices) {
				resultTable[targetColumns[col]]!![y] = Cell(y, col).also {
					it.value = results!!.groups[col + 1]!!.value
					it.stringValue = it.value as String
					it.column = targetColumns[col]
				}
			}
		}
		return resultTable
	}

	override fun uses(column: Int) = initialColumn == column
}