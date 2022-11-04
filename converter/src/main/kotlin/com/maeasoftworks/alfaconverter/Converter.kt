package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import org.xlsx4j.sml.Worksheet
import java.io.ByteArrayInputStream

class Converter(
	private val firstFile: ByteArray,
	private val secondFile: ByteArray,
	conversionJson: String
) {
	private val worksheet = PartName("/xl/worksheets/sheet1.xml")

	private val initialDocument: SpreadsheetMLPackage = SpreadsheetMLPackage.load(ByteArrayInputStream(firstFile))
	private val resultDocument: SpreadsheetMLPackage = SpreadsheetMLPackage.load(ByteArrayInputStream(secondFile))

	private val initialWorksheet: Worksheet = (initialDocument.parts[worksheet] as WorksheetPart).jaxbElement
	private val resultWorksheet: Worksheet = (resultDocument.parts[worksheet] as WorksheetPart).jaxbElement

	internal lateinit var initialTable: Table
	internal lateinit var resultTable: Table

	internal val conversion: Conversion = Json.decodeFromString(conversionJson)
	private lateinit var finalDocument: SpreadsheetMLPackage

	internal fun initialize(): Converter {
		initialTable = Table.create(initialDocument, initialWorksheet)
		resultTable = Table.create(resultDocument, resultWorksheet)
		conversion.register(initialTable, resultTable)
		return this
	}

	fun convert(): ByteArray {
		initialize()
		return if (firstFile.isNotEmpty() && secondFile.isNotEmpty()) ByteArray(1) else ByteArray(0)
	}
}