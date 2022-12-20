package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.xlsx.structure.StringData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.maeasoftworks.alfaconverter.core.model.Source
import com.maeasoftworks.alfaconverter.core.model.Result

/**
 * Action that merges all data from [initialColumns] to [targetColumn] by [pattern].
 * @param initialColumns addresses of columns in [Source] table that will be merged.
 * @param targetColumn address of column in [Result] table that will be filled.
 * @param pattern merging pattern. insertion template format: "${_&lt;address1&gt;_}".
 * @constructor Creates new instance.
 */
@Serializable
@SerialName("merge")
class Merge(
	private val initialColumns: List<ColumnAddress>,
	private val targetColumn: ColumnAddress,
	private val pattern: String
) : Action() {

	override fun run(initialTable: Table, resultTable: Table) {
		val sourceColumns = initialTable[initialColumns]
		for (y in 0 until initialTable.rowsCount) {
			var result = pattern
			var pos = 0
			Table.slice(sourceColumns, y).forEach { cell ->
				// it doesn't work when inlined
				val oldValue = "\${${pos++}}"
				val newValue = cell!!.getString()
				result = result.replace(oldValue, newValue)
			}
			resultTable[targetColumn, y] = StringData(result)
		}
	}
}