package com.maeasoftworks.alfaconverter.actions

import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.Serializable

@Serializable
internal sealed class Action {
	abstract fun run(initialTable: Table, resultTable: Table): Table

	abstract fun uses(column: Int): Boolean
}