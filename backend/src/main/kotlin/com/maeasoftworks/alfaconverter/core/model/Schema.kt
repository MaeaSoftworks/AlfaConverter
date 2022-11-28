package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.datatypes.xsd.ComplexTypePlaceholder
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.UnknownType
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XPrimitive
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XType
import org.jdom2.Element
import org.jdom2.input.SAXBuilder

class Schema(data: String) {
	private val builder = SAXBuilder()
	private val document = builder.build(data.byteInputStream())
	private val root = document.rootElement

	val types = mutableListOf<XType>()
	private val placeholders = mutableMapOf<XType, ComplexTypePlaceholder>()

	init {
		val namespaces = root.namespacesInScope
		if (namespaces.all { it.uri != "http://www.w3.org/2001/XMLSchema" }) {
			throw Exception("File is not valid XSD schema.")
		}
		for (element in root.children) {
			createElement(element)
		}
		setPlaceholders()
		println()
	}

	private fun createElement(element: Element?) {
		if (element?.name == "complexType") {
			createComplexType(element)
		} else if (element?.children?.first()?.name == "complexType") {
			createComplexType(element.children.first())
		}
	}

	private fun createComplexType(complexType: Element) {
		val sequence = complexType.children.first()
		check(sequence.name == "sequence")
		val type =
			XType(complexType.getAttribute("name")?.value ?: complexType.parentElement.getAttribute("name").value)
		for (field in sequence.children) {
			val fieldName = field.getAttributeValue("name")
			val fieldType = field.getAttributeValue("type")
			type.fields[fieldName] = XPrimitive.findPrimitive(fieldType)?.xType?.invoke() ?: ComplexTypePlaceholder(
				fieldType,
				fieldName,
				type
			).register()
		}
		types += type
	}

	private fun setPlaceholders() {
		for (obj in placeholders) {
			types.first { it.name == obj.key.name }.fields[obj.value.fieldName] =
				types.firstOrNull { it.name == obj.value.typeName }?.also { it.dependent++ }
					?: UnknownType(obj.value.typeName)
		}
	}

	private fun ComplexTypePlaceholder.register(): ComplexTypePlaceholder {
		placeholders[this.instance] = this
		return this
	}
}