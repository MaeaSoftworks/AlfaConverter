package com.maeasoftworks.alfaconverter.core.conversions

import com.maeasoftworks.alfaconverter.core.conversions.actions.Action
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Conversion(
	private val actions: @Contextual MutableList<Action>,
	private val typeConversions: @Contextual MutableMap<String, TypeConversion> = mutableMapOf()
) {
	@Transient lateinit var source: Table
	@Transient lateinit var target: Table

	internal fun addAction(action: Action) {
		actions.add(action)
	}

	internal fun addActions(vararg action: Action) {
		actions.addAll(action)
	}

	internal fun addTypeConversion(column: String, conversion: TypeConversion) = typeConversions.set(column, conversion)

	fun register(source: Table, target: Table) {
		this.source = source
		this.target = target
	}

	fun start() {
		for (action in actions.indices) {
			actions[0].run(source, target)
			actions.removeAt(0)
		}

		typeConversions.forEach { (key, conversion) ->
			target[key]!!.changeType(conversion)
		}
	}

	companion object {
		val empty: Conversion
			get() = Conversion(mutableListOf())
	}
}