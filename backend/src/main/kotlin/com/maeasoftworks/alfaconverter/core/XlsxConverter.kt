package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.model.Spreadsheet
import com.maeasoftworks.alfaconverter.core.model.Table

class XlsxConverter {
	private val source: Spreadsheet = Spreadsheet()
	internal val target: Spreadsheet = Spreadsheet()
	internal var conversion: Conversion = Conversion.empty

	constructor(source: ByteArray, target: ByteArray, conversion: Conversion = Conversion.empty) {
		this.source.open(source)
		this.target.open(target)
		this.source.initializeTable()
		this.target.initializeTable()
		this.conversion = conversion
		this.conversion.register(this.source.table, this.target.table)
	}

	constructor(source: Table, target: Table) {
		this.source.table = source
		this.target.table = target
		this.conversion.register(this.source.table, this.target.table)
	}

	fun getHeaders(): List<List<String?>> {
		return source.getHeadersAndExamples() + target.getHeadersAndExamples()
	}

	fun convert(): ByteArray {
		target.clean()
		conversion.start()
		return target.save()
	}
}