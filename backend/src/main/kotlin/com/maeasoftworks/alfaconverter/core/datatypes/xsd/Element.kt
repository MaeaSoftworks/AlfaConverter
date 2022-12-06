package com.maeasoftworks.alfaconverter.core.datatypes.xsd

import kotlinx.serialization.Serializable

@Serializable
class Element(val name: String) {
	lateinit var type: Type

	fun createInstance(): SerializableInstance {
		return type.createInstance()
	}
}