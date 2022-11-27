package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.Serializable

@Serializable
sealed class Action {
	abstract fun run(initialTable: Table, resultTable: Table): Table

	abstract fun isUsing(column: Int): Boolean
}