package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import com.maeasoftworks.alfaconverter.core.model.Schema
import com.maeasoftworks.alfaconverter.core.model.Spreadsheet

class XmlConverter {
	private var document: Spreadsheet
	var schema: Schema
	var conversion: Conversion = Conversion.empty

	constructor(
		document: ByteArray,
		schema: Element,
		conversion: Conversion = Conversion.empty
	) {
		this.document = Spreadsheet().open(document)
		this.document.initializeTable()
		this.schema = Schema(schema)
		this.conversion = conversion
		this.conversion.register(this.document.table, this.schema.table)
	}

	constructor(document: ByteArray, xsd: ByteArray) {
		this.document = Spreadsheet().open(document)
		this.document.initializeTable()
		schema = Schema(String(xsd))
	}

	fun getHeadersAndFirstLine(): List<List<String?>> {
		return document.getHeadersAndExamples()
	}

	fun getSchema(): Element {
		return schema.elements.first { it.type.dependent == 0 }
	}

	fun convert(): String {
		conversion.start()
		return schema.save()
	}

	fun getEndpoints(): List<String> {
		return schema.convertElementsToHeaders()
	}
}