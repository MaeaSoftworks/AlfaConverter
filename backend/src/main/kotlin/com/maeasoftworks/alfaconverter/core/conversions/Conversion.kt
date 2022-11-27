package com.maeasoftworks.alfaconverter.core.conversions

import com.maeasoftworks.alfaconverter.core.conversions.actions.Action
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.model.Table
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Conversion(
	private val actions: @Contextual MutableList<Action>,
	@SerialName("type-conversions")
	private val typeConversions: @Contextual MutableMap<Int, TypeConversion> = mutableMapOf()
) {
	@Transient
	lateinit var source: Table
	@Transient
	lateinit var target: Table

	internal fun addAction(action: Action) = actions.add(action)

	internal fun addTypeConversion(column: Int, conversion: TypeConversion) = typeConversions.set(column, conversion)

	fun register(source: Table, target: Table) {
		this.source = source
		this.target = target
		this.source.columns.forEach { (pos, column) ->
			actions.forEach {
				if (it.isUsing(pos)) {
					column.hasAction = true
				}
			}
		}
		this.source.columns.forEach { (pos, column) ->
			if (!column.hasAction) {
				actions.add(Bind(pos, pos))
			}
		}
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