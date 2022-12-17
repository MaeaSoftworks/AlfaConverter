package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SimpleType")
class SimpleType : Type {
	constructor(name: String) : super(name)

	override fun createInstance(): SerializableInstance {
		return SerializableInstance(name).also { it.type = this }
	}
}