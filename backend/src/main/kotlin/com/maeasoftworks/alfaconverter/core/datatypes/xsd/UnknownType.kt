package com.maeasoftworks.alfaconverter.core.datatypes.xsd

import kotlinx.serialization.Serializable

@Serializable
class UnknownType(override val name: String) : Type() {
	override fun createInstance(): SerializableInstance {
		throw Exception("What?")
	}
}