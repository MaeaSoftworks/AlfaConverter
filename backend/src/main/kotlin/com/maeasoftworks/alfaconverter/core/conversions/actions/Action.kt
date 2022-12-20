package com.maeasoftworks.alfaconverter.core.conversions.actions

import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.Serializable
import com.maeasoftworks.alfaconverter.core.model.Source
import com.maeasoftworks.alfaconverter.core.model.Result

/**
 * Base type for all actions.
 */
@Serializable
sealed class Action {
	/**
	 * Action body. All doings must get data from [initialTable], handle it and put into [resultTable].
	 * @param initialTable table from [Source]
	 * @param resultTable table from [Result]
	 */
	abstract fun run(initialTable: Table, resultTable: Table)
}