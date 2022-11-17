package com.maeasoftworks.alfaconverter.actions

import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("split")
internal class Split(
	@SerialName("initial-column")
	val initialColumn: Int,
	@SerialName("target-columns")
	val targetColumns: List<Int>,
	private val pattern: String
) : Action() {
	@Transient
	private val regex = Regex(pattern)

	override fun run(initialTable: Table, resultTable: Table): Table {
		val initialColumn = initialTable[initialColumn]
		for (y in 1..initialTable.rowsCount) {
			val results = regex.matchEntire(initialColumn?.get(y)?.value!!.toString().trim())!!.groups.filterNotNull().drop(1)
			for (col in results.indices) {
				resultTable[targetColumns[col]]!![y] = Cell(y, col).also {
					it.value = results[col].value
					it.stringValue = it.value as String
					it.column = targetColumns[col]
				}
			}
		}
		return resultTable
	}

	override fun isUsing(column: Int) = initialColumn == column || targetColumns.contains(column)
}