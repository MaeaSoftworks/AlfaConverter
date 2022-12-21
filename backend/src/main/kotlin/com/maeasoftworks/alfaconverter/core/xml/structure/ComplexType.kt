package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jdom2.Element as XsdElement

@Serializable
@SerialName("ComplexType")
class ComplexType : Type {
	@Transient private var registration: ((TypePlaceholder) -> Unit)? = null

	constructor(name: String, registration: ((TypePlaceholder) -> Unit)? = null) : super(name) {
		this.registration = registration
	}

	override fun createInstance(): Type {
		return Instance(name).apply {
			for (field in fields) {
				fields[field.key] = field.value.createInstance()
			}
			for (attribute in attributes) {
				attributes[attribute.key] = attribute.value.createInstance()
			}
		}
	}

	fun sequenceToFields(sequence: XsdElement, prefix: String) {
		for (field in sequence.children) {
			val fieldName: String? = field.getAttributeValue("name")
			val fieldType: String? = field.getAttributeValue("type")
			val refField: String? = field.getAttributeValue("ref")
			if (fieldName != null && fieldType != null) {
				fields[fieldName] = Primitive.findPrimitive(fieldType, prefix) ?: TypePlaceholder(name, fieldType, fieldName = fieldName).also(registration!!)
			} else if (refField != null) {
				fields[refField] = TypePlaceholder(name, refField, true).also(registration!!)
			}
		}
	}

	fun collectAttributes(element: XsdElement, prefix: String) {
		val attributes = element.children.filter { it.name == "attribute" }
		for (attribute in attributes) {
			val attributeName = attribute.getAttributeValue("name")
			val attributeType = attribute.getAttributeValue("type")
			this.attributes[attributeName] = Primitive.findPrimitive(attributeType, prefix) ?: TypePlaceholder(name, attributeName, true).also(registration!!)
		}
	}
}