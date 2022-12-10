package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SimpleType")
class SimpleType(override val name: String) : Type() {
	override fun createInstance(): SerializableInstance {
		return SerializableInstance(name).also { it.type = this }
	}
}