package com.maeasoftworks.alfaconverter.conversions.actions

import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.Serializable

@Serializable
internal sealed class Action {
	abstract fun run(initialTable: Table, resultTable: Table): Table

	abstract fun isUsing(column: Int): Boolean
}