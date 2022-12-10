package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.xlsx.structure.SString
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("split")
class Split(
	private val initialColumn: String,
	private val targetColumns: List<String>,
	@Suppress("CanBeParameter")
	private val pattern: String
) : Action() {
	@Transient
	private val regex = Regex(pattern)

	override fun run(initialTable: Table, resultTable: Table) {
		val initialColumn = initialTable[initialColumn]!!
		for (row in 0 until initialTable.rowsCount) {
			val results = regex.matchEntire(initialColumn[row].getString())!!.groups.filterNotNull().drop(1)
			for (column in results.indices) {
				resultTable[targetColumns[column], row] = SString(results[column].value)
			}
		}
	}
}