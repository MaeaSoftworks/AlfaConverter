package com.maeasoftworks.alfaconverter.core.xml

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xlsx.Spreadsheet
import com.maeasoftworks.alfaconverter.core.xml.structure.Element

class XmlConverter: Converter {
	private var document: Spreadsheet
	var schema: Schema

	override val source: Table
		get() = document.table
	override val target: Table
		get() = schema.table

	constructor(document: ByteArray, xsd: ByteArray) {
		this.document = Spreadsheet().open(document)
		this.document.initializeTable()
		schema = Schema(String(xsd))
	}

	constructor(document: ByteArray, schema: Element, conversion: Conversion = Conversion()) {
		this.document = Spreadsheet().open(document)
		this.document.initializeTable()
		this.schema = Schema(schema)
		this.conversion = conversion
	}

	fun getHeaders() = document.getHeaders()

	fun getExamples() = document.getExamples()

	fun getSchema() = schema.elements.first { it.type.dependent == 0 }

	fun getEndpoints() = schema.convertElementsToHeaders()

	fun convert(): String {
		executeActions()
		return schema.save()
	}
}