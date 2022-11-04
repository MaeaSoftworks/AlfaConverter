package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.actions.Action
import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal class Conversion(private val actions: @Contextual MutableList<Action>) {
	@Transient
	lateinit var initialTable: Table

	@Transient
	lateinit var resultTable: Table

	internal fun addAction(action: Action) = actions.add(action)

	fun register(initialTable: Table, resultTable: Table) {
		this.initialTable = initialTable
		this.resultTable = resultTable
		this.initialTable.columns.forEach { (pos, column) ->
			actions.forEach {
				if (it.uses(pos)) {
					column.hasAction = true
				}
			}
		}
	}

	fun start() {
		for (action in actions.indices) {
			actions[0].run(initialTable, resultTable)
			actions.removeAt(0)
		}
	}
}