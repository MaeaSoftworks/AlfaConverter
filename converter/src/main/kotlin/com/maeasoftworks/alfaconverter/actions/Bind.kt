package com.maeasoftworks.alfaconverter.actions

import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("bind")
class Bind(
	@SerialName("initial-column")
	val initialColumn: Int,
	@SerialName("target-column")
	val targetColumn: Int
) : Action() {

	override fun run(initialTable: Table, resultTable: Table): Table {
		initialTable[initialColumn]?.cells?.values?.forEach { resultTable[targetColumn]?.cells?.put(it.row, it) }
		return resultTable
	}
}