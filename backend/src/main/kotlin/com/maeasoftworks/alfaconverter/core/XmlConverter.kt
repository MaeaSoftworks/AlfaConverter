package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
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

	fun convert(): ByteArray {
		schema = Schema(String(xsd))
		return ByteArray(0)
	}
}