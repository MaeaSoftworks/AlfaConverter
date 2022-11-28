package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XType
import com.maeasoftworks.alfaconverter.core.model.Schema
import com.maeasoftworks.alfaconverter.core.model.Spreadsheet

class XmlConverter(
	document: ByteArray,
	private var xsd: ByteArray,
	private var conversion: Conversion = Conversion.empty
) {
	private var document: Spreadsheet
	private lateinit var schema: Schema

	init {
		this.document = Spreadsheet().open(document)
		this.document.initializeTable()
	}

	fun getHeadersAndFirstLine(): List<List<String?>> {
		return document.getHeadersAndExamples()
	}

	fun getSchema(): List<XType> {
		schema = Schema(String(xsd))
		return schema.types.filter { it.dependent == 0 }
	}

	fun convert(): String {
		schema = Schema(String(xsd))
		return ""
	}
}