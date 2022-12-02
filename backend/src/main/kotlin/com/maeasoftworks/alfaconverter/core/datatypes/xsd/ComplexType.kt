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

	fun sequenceToFields(sequence: XsdElement, prefix: String) {
		for (field in sequence.children) {
			val fieldName: String? = field.getAttributeValue("name")
			val fieldType: String? = field.getAttributeValue("type")
			val refField: String? = field.getAttributeValue("ref")
			if (fieldName != null && fieldType != null) {
				fields[fieldName] = XPrimitive.findPrimitive(fieldType, prefix)?.element?.invoke()
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
			this.attributes[attributeName] = XPrimitive.findPrimitive(attributeType, prefix)?.element?.invoke()
				?: registration!!(TypePlaceholder(name, attributeName, true))
		}
	}

	fun build(builder: DSL.() -> Unit): ComplexType {
		builder(this.DSL(this))
		return this
	}

	inner class DSL(private val type: ComplexType) {

		infix fun String.of(type: Type) {
			this@DSL.type.fields[this] = type
		}

		infix fun String.of(type: XPrimitive) {
			this@DSL.type.fields[this] = type.element()
		}
	}
}