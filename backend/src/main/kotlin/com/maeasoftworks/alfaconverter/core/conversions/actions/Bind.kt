package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.maeasoftworks.alfaconverter.core.model.Source
import com.maeasoftworks.alfaconverter.core.model.Result

/**
 * Action that moves all data from [initialColumn] to [targetColumn].
 * @param initialColumn address of column in [Source] table that will be bound.
 * @param targetColumn address of column in [Result] table that will be filled.
 * @constructor Creates new instance.
 */
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