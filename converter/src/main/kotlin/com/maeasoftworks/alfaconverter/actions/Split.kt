package com.maeasoftworks.alfaconverter.actions

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
		TODO("Not yet implemented")
	}
}