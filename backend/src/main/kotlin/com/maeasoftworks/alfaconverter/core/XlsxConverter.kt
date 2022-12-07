package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.model.Spreadsheet
import com.maeasoftworks.alfaconverter.core.model.Table

class XlsxConverter {
	private val source: Spreadsheet = Spreadsheet()
	val target: Spreadsheet = Spreadsheet()
	var conversion: Conversion = Conversion.empty

	constructor(source: Table, target: Table) {
		this.source.table = source
		this.target.table = target
		this.conversion.register(this.source.table, this.target.table)
	}

	constructor(source: ByteArray, target: ByteArray, conversion: Conversion = Conversion.empty) {
		this.source.open(source)
		this.target.open(target)
		this.source.initializeTable()
		this.target.initializeTable()
		this.conversion = conversion
		this.conversion.register(this.source.table, this.target.table)
	}

	fun getHeaders() = Pair(source.getHeaders(), target.getHeaders())

	fun getExamples() = Pair(source.getExamples(), target.getExamples())

	fun convert(): ByteArray {
		target.clean()
		conversion.start()
		return target.save()
	}
}