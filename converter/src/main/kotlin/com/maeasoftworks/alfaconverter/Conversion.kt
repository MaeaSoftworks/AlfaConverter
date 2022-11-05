package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.actions.Action
import com.maeasoftworks.alfaconverter.model.BondedPair
import com.maeasoftworks.alfaconverter.wrappers.Document
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

	fun register(documents: BondedPair<Document>) {
		this.initialTable = documents.master.table
		this.resultTable = documents.slave.table
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

	companion object {
		val empty: Conversion
			get() = Conversion(mutableListOf())
	}
}