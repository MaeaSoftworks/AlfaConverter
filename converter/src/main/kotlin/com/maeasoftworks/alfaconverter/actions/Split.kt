package com.maeasoftworks.alfaconverter.actions

import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("split")
class Split(
	@SerialName("initial-column")
	val initialColumn: Int,
	@SerialName("target-columns")
	val targetColumns: List<Int>,
	val pattern: String
) : Action() {
	override fun run(initialTable: Table, resultTable: Table): Table {
		val initialColumn = initialTable[initialColumn]
		for (y in 0 until initialTable.rowsCount) {
			val results = Regex(pattern).split(initialColumn?.get(y)?.value!!.toString())
			for (col in resultTable[targetColumns].indices) {
				for (a in resultTable[targetColumns].indices) {
					resultTable[targetColumns][a] = Cell(y, col).also { it.value = results[col] }
				}
			}
		}
		return resultTable
	}

	override fun uses(column: Int) = initialColumn == column
}