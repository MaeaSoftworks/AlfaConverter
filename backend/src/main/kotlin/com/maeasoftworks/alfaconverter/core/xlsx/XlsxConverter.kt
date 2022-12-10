package com.maeasoftworks.alfaconverter.core.xlsx

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.model.Table

class XlsxConverter: Converter {
	private val sourceSpreadsheet: Spreadsheet = Spreadsheet()
	val targetSpreadsheet: Spreadsheet = Spreadsheet()
	override val source: Table
		get() = sourceSpreadsheet.table
	override val target: Table
		get() = targetSpreadsheet.table

	constructor(source: Table, target: Table) {
		this.sourceSpreadsheet.table = source
		this.targetSpreadsheet.table = target
	}

	constructor(source: ByteArray, target: ByteArray, conversion: Conversion = Conversion()) {
		this.sourceSpreadsheet.open(source)
		this.targetSpreadsheet.open(target)
		this.sourceSpreadsheet.initializeTable()
		this.targetSpreadsheet.initializeTable()
		this.conversion = conversion
	}

	fun getHeaders() = Pair(sourceSpreadsheet.getHeaders(), targetSpreadsheet.getHeaders())

	fun getExamples() = Pair(sourceSpreadsheet.getExamples(), targetSpreadsheet.getExamples())

	fun convert(): ByteArray {
		targetSpreadsheet.clean()
		executeActions()
		return targetSpreadsheet.save()
	}
}