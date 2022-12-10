package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.xlsx.structure.StringData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("merge")
class Merge(
	private val initialColumns: List<String>,
	private val targetColumn: String,
	private val pattern: String
) : Action() {

	override fun run(initialTable: Table, resultTable: Table) {
		val sourceColumns = initialTable[initialColumns]
		for (y in 0 until initialTable.rowsCount) {
			var result = pattern
			var pos = 0
			Table.slice(sourceColumns, y).forEach { cell ->
				// it doesn't work when inlined
				val oldValue = "\${${sourceColumns[pos++].name}}"
				val newValue = cell!!.getString()
				result = result.replace(oldValue, newValue)
			}
			resultTable[targetColumn, y] = StringData(result)
		}
	}
}