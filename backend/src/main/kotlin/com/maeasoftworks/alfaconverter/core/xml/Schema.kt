package com.maeasoftworks.alfaconverter.core.xml

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xml.structure.*
import org.jdom2.Document
import org.jdom2.input.SAXBuilder
import org.jdom2.Element as XsdElement

class Schema {
	var elements = mutableListOf<Element>()
	var types = mutableListOf<Type>()
	lateinit var table: Table

	private val builder = SAXBuilder()
	private var document: Document? = null
	private lateinit var prefix: String
	private val root: XsdElement?
		get() = document?.rootElement

	private val placeholders = mutableListOf<TypePlaceholder>()

	constructor(data: String) {
		document = builder.build(data.byteInputStream())
		prefix = root!!.namespacesInScope.firstOrNull { it.uri == "http://www.w3.org/2001/XMLSchema" }?.prefix
			?: throw Exception("Invalid markup")
		for (element in root!!.children) {
			handleXsdElement(element)
		}
		setPlaceholders()
	}

	constructor(schema: Element) {
		elements = mutableListOf(schema)
		table = Table().fill {
			for (header in convertElementsToHeaders()) {
				column(header)
			}
		}
	}

	private fun handleXsdElement(element: XsdElement) {
		if (element.name == "complexType") {
			createComplexType(element, element.getAttributeValue("name"))
		} else if (element.name == "element") {
			createElement(element)
		}
	}

	private fun createElement(xsdElement: XsdElement) {
		val element = Element(
			xsdElement.getAttribute("name").value
		)
		element.type = createType(xsdElement.children.first(), element.name)
		elements += element
	}

	private fun createType(typeDeclaration: XsdElement, name: String): Type {
		return when (typeDeclaration.name) {
			"simpleType" -> {
				TODO()
			}

			"complexType" -> {
				createComplexType(typeDeclaration, name)
			}

			else -> throw Exception("Invalid schema")
		}
	}

	private fun createComplexType(typeDeclaration: XsdElement, name: String): ComplexType {
		val type = ComplexType(name, ::register)
		var isSequenceBody = false
		typeDeclaration.children.firstOrNull { it.name == "sequence" }?.let {
			type.sequenceToFields(it, prefix)
			isSequenceBody = true
		}
		if (!isSequenceBody) {
			if (typeDeclaration.children.any { it.name == "attribute" }) {
				type.collectAttributes(typeDeclaration, prefix)
			}
		}
		types += type
		return type
	}

	private fun setPlaceholders() {
		for (placeholder in placeholders) {
			types.first { it.name == placeholder.targetTypeName }.let { type ->
				type as ComplexType
				val fieldName = if (placeholder.isRef) placeholder.awaitedTypeName else placeholder.fieldName!!
				if (fieldName in type.fields.keys) {
					type.fields[fieldName] = types.firstOrNull {
						it.name == placeholder.awaitedTypeName
					}?.also { it.dependent++ } ?: UnknownType(placeholder.targetTypeName)
				} else {
					if (fieldName in type.attributes.keys) {
						type.attributes[placeholder.awaitedTypeName] = types.firstOrNull {
							it.name == placeholder.awaitedTypeName
						}?.also { it.dependent++ } ?: UnknownType(placeholder.targetTypeName)
					}
				}
			}
		}
	}

	fun convertElementsToHeaders(): List<ColumnAddress> {
		val result = mutableListOf<ColumnAddress>()
		convertElementToHeaders(elements.first { it.type.dependent == 0 }.type, result)
		return result
	}

	private fun convertElementToHeaders(
		type: Type,
		result: MutableList<ColumnAddress>,
		current: ColumnAddress? = null
	) {
		val pref = current ?: mutableListOf(type.name)
		for (field in type.fields) {
			if (field.value !is ComplexType) {
				result += pref + field.key
			} else {
				convertElementToHeaders(field.value, result, pref + field.key)
			}
		}
		for (attribute in (type).attributes) {
			result += pref + attribute.key
		}
	}

	private fun register(placeholder: TypePlaceholder): TypePlaceholder {
		placeholders += placeholder
		return placeholder
	}

	fun save(): String {
		val root = elements.first().type
		val example = root.fields.values.first()
		val instances = mutableListOf<Type>()
		for (y in 0 until table.rowsCount) {
			val instance = example.clone()
			instances += instance

			for (x in table.columns.indices) {
				val path = table.columns[x].name.drop(2)
				var endpoint = instance
				for (f in path) {
					endpoint = endpoint[f]
				}
				endpoint.value = table.columns[x].cells[y]
			}
		}
		root.collection = instances
		return root.toXml()
	}
}