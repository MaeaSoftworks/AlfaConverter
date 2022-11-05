package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.actions.Action
import com.maeasoftworks.alfaconverter.actions.Bind
import com.maeasoftworks.alfaconverter.model.BondedPair
import com.maeasoftworks.alfaconverter.wrappers.Document
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal class Conversion(private val actions: @Contextual MutableList<Action>) {
	@Transient
	lateinit var document: BondedPair<Document>

	internal fun addAction(action: Action) = actions.add(action)

	fun register(document: BondedPair<Document>) {
		this.document = document
		this.document.master.table.columns.forEach { (pos, column) ->
			actions.forEach {
				if (it.uses(pos)) {
					column.hasAction = true
				}
			}
		}
		this.document.master.table.columns.forEach { (pos, column) ->
			if (!column.hasAction) {
				actions.add(Bind(pos, pos))
			}
		}
	}

	fun start() {
		for (action in actions.indices) {
			actions[0].run(document.master.table, document.slave.table)
			actions.removeAt(0)
		}
	}

	companion object {
		val empty: Conversion
			get() = Conversion(mutableListOf())
	}
}