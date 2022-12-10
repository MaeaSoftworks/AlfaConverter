package com.maeasoftworks.alfaconverter.core.xml.structure

import com.maeasoftworks.alfaconverter.core.xlsx.structure.SObject

class SerializableInstance(val name: String) : Cloneable {
	lateinit var type: Type
	var value: SObject? = null
	var collection: List<SerializableInstance>? = null
	val fields = mutableMapOf<String, SerializableInstance>()
	val attributes = mutableMapOf<String, SerializableInstance>()

	operator fun get(path: String): SerializableInstance {
		return if (fields.contains(path)) {
			fields[path]!!
		} else if (attributes.contains(path)) {
			attributes[path]!!
		} else {
			throw IndexOutOfBoundsException("Endpoint $path was not found in instance $name")
		}
	}

	public override fun clone(): SerializableInstance {
		val clone = SerializableInstance(name)
		clone.type = type
		for (i in fields) {
			clone.fields[i.key] = i.value.clone()
		}
		for (i in attributes) {
			clone.attributes[i.key] = i.value.clone()
		}
		return clone
	}

	private enum class Target {
		FIELD, ATTRIBUTE
	}

	fun toXml() = toXml(null, null)

	private fun toXml(targetName: String?, target: Target?): String {
		if (type.isPrimitive) {
			return when (target) {
				Target.FIELD -> {
					"<${targetName}>${
						value!!.getXmlRepresentation().removePrefix("\"").removeSuffix("\"")
					}</${targetName}>"
				}

				Target.ATTRIBUTE -> {
					value!!.getXmlRepresentation()
				}

				else -> value!!.getXmlRepresentation()
			}
		}
		return if (collection != null) {
			"<${name}>${collection!!.joinToString { it.toXml() }}</${name}>"
		} else {
			if (fields.isNotEmpty()) {
				"<${name}>${fields.map { (n, i) -> i.toXml(n, Target.FIELD) }.joinToString("")}</${name}>"
			} else if (attributes.isNotEmpty()) {
				"<${name} ${attributes.map { (n, i) -> "$n=${i.toXml(n, Target.ATTRIBUTE)}" }.joinToString(" ")}/>"
			} else {
				"<${name}/>"
			}
		}
	}
}