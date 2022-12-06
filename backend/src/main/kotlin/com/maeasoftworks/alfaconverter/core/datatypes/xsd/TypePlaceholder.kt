package com.maeasoftworks.alfaconverter.core.datatypes.xsd

class TypePlaceholder(
	val targetTypeName: String,
	val awaitedTypeName: String,
	val isRef: Boolean = false,
	val fieldName: String? = null
) : Type() {
	override val name = "\$\$placeholder\$\$"
	override fun createInstance(): SerializableInstance {
		throw Exception("How?")
	}
}