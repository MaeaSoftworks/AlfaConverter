package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.xlsx.structure.StringData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import com.maeasoftworks.alfaconverter.core.model.Source
import com.maeasoftworks.alfaconverter.core.model.Result

/**
 * Action that splits all data from [initialColumn] to [targetColumns].
 * @param initialColumn address of column in [Source] table that will be split.
 * @param targetColumns addresses of columns in [Result] table that will be filled.
 * @param pattern regex pattern.
 * @constructor Creates new instance.
 */
@Serializable
@SerialName("split")
class Split(
	private val initialColumn: ColumnAddress,
	private val targetColumns: List<ColumnAddress>,
	@Suppress("CanBeParameter")
	private val pattern: String
) : Action() {
	@Transient
	private val regex = Regex(pattern)

	override fun run(initialTable: Table, resultTable: Table) {
		val initialColumn = initialTable[initialColumn]!!
		for (row in 0 until initialTable.rowsCount) {
			val results = regex.matchEntire(initialColumn[row]!!.getString())!!.groups.filterNotNull().drop(1)
			for (column in results.indices) {
				resultTable[targetColumns[column], row] = StringData(results[column].value)
			}
		}
	}
}