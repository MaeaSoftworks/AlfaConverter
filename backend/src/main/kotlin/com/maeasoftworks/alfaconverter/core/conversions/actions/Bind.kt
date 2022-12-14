package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("bind")
class Bind(
	private val initialColumn: ColumnAddress,
	private val targetColumn: ColumnAddress
) : Action() {

	override fun run(initialTable: Table, resultTable: Table) {
		for (cell in initialTable[initialColumn]?.cells!!) {
			resultTable[targetColumn]?.cells?.add(cell)
		}
	}
}