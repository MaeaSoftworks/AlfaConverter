package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("join")
class Join(
	private val sourceColumns: List<ColumnAddress>,
	private val targetColumn: ColumnAddress,
	private val patterns: List<String>,
	private val regexp: String
) : Action() {
	override fun run(initialTable: Table, resultTable: Table) {
		if (sourceColumns.size == 1) {
			runSplit(initialTable, resultTable)
		} else {
			runMergeAndSplit(initialTable, resultTable)
		}
	}

	private fun runSplit(initialTable: Table, resultTable: Table) {

	}

	private fun runMergeAndSplit(initialTable: Table, resultTable: Table) {

	}
}