package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.conversions.at
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.*
import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.input.SAXBuilder

class Schema {
	var types = mutableListOf<XType>()
	lateinit var table: Table

	private val builder = SAXBuilder()
	private var document: Document? = null
	private val root : Element?
		get () = document?.rootElement

	private val placeholders = mutableMapOf<XType, ComplexTypePlaceholder>()

	constructor(data: String) {
		document = builder.build(data.byteInputStream())
		val namespaces = root!!.namespacesInScope
		if (namespaces.all { it.uri != "http://www.w3.org/2001/XMLSchema" }) {
			throw Exception("File is not valid XSD schema.")
		}
		for (element in root!!.children) {
			createElement(element)
		}
		setPlaceholders()
	}

	constructor(schema: List<XType>) {
		types = schema.toMutableList()
		table = Table().fill {
			for (header in convertTypesToHeaders()) {
				header(Table.Cell(header[1] at header[0], 0) with SString("${header[0]}\$${header[1]}"))
			}
		}
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
			types.first { it.typename == obj.key.typename }.fields[obj.value.fieldName] =
				types.firstOrNull { it.typename == obj.value.typeName }?.also { it.dependent++ }
					?: UnknownType(obj.value.typeName)
		}
	}

	private fun convertTypesToHeaders(): List<List<String>> {
		val result = mutableListOf<List<String>>()
		for (type in types) {
			convertTypeToHeaders(type, result)
		}
		return result
	}

	private fun convertTypeToHeaders(type: XType, result: MutableList<List<String>>) {
		for (field in type.fields) {
			if (field.value.complexity != Complexity.COMPLEX_TYPE) {
				result.add(listOf(type.typename, field.key))
			} else {
				convertTypeToHeaders(field.value, result)
			}
		}
	}

	private fun ComplexTypePlaceholder.register(): ComplexTypePlaceholder {
		placeholders[this.instance] = this
		return this
	}
}