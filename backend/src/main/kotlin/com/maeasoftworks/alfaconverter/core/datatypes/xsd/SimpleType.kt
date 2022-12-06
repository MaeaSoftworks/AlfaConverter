package com.maeasoftworks.alfaconverter.core.datatypes.xsd

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SimpleType")
class SimpleType(override val name: String) : Type() {
	override fun createInstance(): SerializableInstance {
		return SerializableInstance(name).also { it.type = this }
	}
}