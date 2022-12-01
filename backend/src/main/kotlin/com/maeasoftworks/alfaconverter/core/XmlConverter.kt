package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XType
import com.maeasoftworks.alfaconverter.core.model.Schema
import com.maeasoftworks.alfaconverter.core.model.Spreadsheet

class XmlConverter {
	private var document: Spreadsheet
	var schema: Schema
	var conversion: Conversion = Conversion.empty

	constructor(document: ByteArray,
	            schema: List<XType>,
	            conversion: Conversion = Conversion.empty) {
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

	fun getSchema(): List<XType> {
		return schema.types.filter { it.dependent == 0 }
	}

	fun convert(): String {
		return ""
	}
}