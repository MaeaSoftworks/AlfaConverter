package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("Type")
sealed class Type(val name: String) {
	@Transient
	var dependent = 0

	val isPrimitive: Boolean
		get() = Primitive.findPrimitive(name, ":") != null

	open fun createInstance(): SerializableInstance {
		return SerializableInstance(name).also { it.type = this }
	}
}