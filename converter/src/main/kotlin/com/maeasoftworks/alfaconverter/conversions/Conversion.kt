package com.maeasoftworks.alfaconverter.conversions

import com.maeasoftworks.alfaconverter.conversions.actions.Action
import com.maeasoftworks.alfaconverter.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.model.BondedPair
import com.maeasoftworks.alfaconverter.model.documents.Document
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal class Conversion(
	private val actions: @Contextual MutableList<Action>,
	@SerialName("type-conversions")
	private val typeConversions: @Contextual MutableMap<Int, TypeConversion> = mutableMapOf()
) {
	@Transient
	lateinit var document: BondedPair<Document>

	internal fun addAction(action: Action) = actions.add(action)

	internal fun addTypeConversion(column: Int, conversion: TypeConversion) = typeConversions.set(column, conversion)

	fun register(document: BondedPair<Document>) {
		this.document = document
		this.document.master.table.columns.forEach { (pos, column) ->
			actions.forEach {
				if (it.isUsing(pos)) {
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

		typeConversions.forEach { (key, conversion) ->
			document.slave.table[key]!!.changeType(conversion)
		}
	}

	companion object {
		val empty: Conversion
			get() = Conversion(mutableListOf())
	}
}