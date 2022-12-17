package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jdom2.Element as XsdElement

@Serializable
@SerialName("ComplexType")
class ComplexType : Type {
	val fields = mutableMapOf<String, Type>()
	val attributes = mutableMapOf<String, Type>()
	@Transient
	private var registration: ((TypePlaceholder) -> TypePlaceholder)? = null

	constructor(
		name: String,
		registration: ((TypePlaceholder) -> TypePlaceholder)? = null
	) : super(name) {
		this.registration = registration
	}

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
				fields[fieldName] = Primitive.findPrimitive(fieldType, prefix)
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
			this.attributes[attributeName] = Primitive.findPrimitive(attributeType, prefix)
				?: registration!!(TypePlaceholder(name, attributeName, true))
		}
	}
}