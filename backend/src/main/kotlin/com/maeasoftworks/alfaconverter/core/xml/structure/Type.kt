package com.maeasoftworks.alfaconverter.core.xml.structure

import com.maeasoftworks.alfaconverter.core.xlsx.structure.Data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("Type")
sealed class Type(val name: String) : Cloneable {
	@Transient var dependent = 0
	@Transient var value: Data? = null
	@Transient var collection: List<Type>? = null
	val fields = mutableMapOf<String, Type>()
	val attributes = mutableMapOf<String, Type>()


	operator fun get(path: String): Type {
		return if (fields.contains(path)) {
			fields[path]!!
		} else if (attributes.contains(path)) {
			attributes[path]!!
		} else {
			throw IndexOutOfBoundsException("Endpoint $path was not found in instance $name")
		}
	}

	public override fun clone(): Type {
		val clone: Type = Instance(name)
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
		if (Primitive.findPrimitive(name, ":") != null) {
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


	open fun createInstance(): Type {
		return Instance(name)
	}

	class Instance(name: String): Type(name)
}