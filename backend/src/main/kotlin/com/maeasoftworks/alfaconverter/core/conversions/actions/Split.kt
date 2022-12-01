package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.model.Table.*
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import com.maeasoftworks.alfaconverter.core.conversions.Target
import com.maeasoftworks.alfaconverter.core.conversions.pos

@Serializable
@SerialName("split")
class Split(
	@SerialName("initial-column")
	val initialColumn: Target,
	@SerialName("target-columns")
	val targetColumns: List<Target>,
	@Suppress("CanBeParameter")
	private val pattern: String
) : Action() {
	@Transient
	private val regex = Regex(pattern)

	override fun run(initialTable: Table, resultTable: Table) {
		val initialColumn = initialTable[initialColumn]
		for (y in 1..initialTable.rowsCount) {
			val results =
				regex.matchEntire(initialColumn?.get(y)?.value!!.getString().trim())!!.groups.filterNotNull().drop(1)
			for (col in results.indices) {
				resultTable[targetColumns[col]]!![y] = Cell(col.pos, y).also {
					it.value = SString(results[col].value)
					it.column = targetColumns[col]
				}
			}
		}
	}

	override fun isUsing(column: Target) = initialColumn == column || targetColumns.contains(column)
}