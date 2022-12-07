package com.maeasoftworks.alfaconverter.core.datatypes.xsd

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jdom2.Element as XsdElement

@Serializable
@SerialName("ComplexType")
class ComplexType(
	override val name: String,
	@Transient
	private val registration: ((TypePlaceholder) -> TypePlaceholder)? = null
) : Type() {
	val fields = mutableMapOf<String, Type>()
	val attributes = mutableMapOf<String, Type>()

	override fun createInstance(): SerializableInstance {
		val instance = SerializableInstance(name).also { it.type = this }
		for (field in fields) {
			instance.fields[field.key] = field.value.createInstance()
		}
		for (attribute in attributes) {
			instance.attributes[attribute.key] = attribute.value.createInstance()
		}
		return instance
	}

	fun sequenceToFields(sequence: XsdElement, prefix: String) {
		for (field in sequence.children) {
			val fieldName: String? = field.getAttributeValue("name")
			val fieldType: String? = field.getAttributeValue("type")
			val refField: String? = field.getAttributeValue("ref")
			if (fieldName != null && fieldType != null) {
				fields[fieldName] = Primitive.findPrimitive(fieldType, prefix)?.element?.invoke()
					?: registration!!(TypePlaceholder(name, fieldType, fieldName = fieldName))
			} else if (refField != null) {
				fields[refField] = registration!!(TypePlaceholder(name, refField, true))
			}
		}
	}

	fun collectAttributes(element: XsdElement, prefix: String) {
		val attributes = element.children.filter { it.name == "attribute" }
		for (attribute in attributes) {
			val attributeName = attribute.getAttributeValue("name")
			val attributeType = attribute.getAttributeValue("type")
			this.attributes[attributeName] = Primitive.findPrimitive(attributeType, prefix)?.element?.invoke()
				?: registration!!(TypePlaceholder(name, attributeName, true))
		}
	}

	fun build(builder: Builder.() -> Unit): ComplexType {
		builder(this.Builder())
		return this
	}

	inner class Builder {
		infix fun String.of(type: Type) {
			this@ComplexType.fields[this] = type
		}

		infix fun String.of(type: Primitive) {
			this@ComplexType.fields[this] = type.element()
		}
	}
}