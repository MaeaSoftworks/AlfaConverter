package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.*
import com.maeasoftworks.alfaconverter.core.model.Table.*
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.Styles
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import org.xlsx4j.jaxb.Context
import org.xlsx4j.sml.STCellType
import org.xlsx4j.sml.Worksheet
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Spreadsheet {
	private lateinit var document: SpreadsheetMLPackage
	private lateinit var worksheet: Worksheet
	internal lateinit var table: Table

	fun open(file: ByteArray): Spreadsheet {
		document = SpreadsheetMLPackage.load(ByteArrayInputStream(file))
		worksheet = (document.parts[sheet] as WorksheetPart).jaxbElement
		return this
	}

	fun initializeTable() {
		if (::table.isInitialized && table.isInitialized) {
			return
		}
		table = Table()
		for (columnPos in worksheet.sheetData.row[0].c.indices) {
			val sObj = extractValue(worksheet.sheetData.row[0].c[columnPos])
			val column = Column(sObj.getString().trim())
			table.columns.add(column)
			table.headers.add(sObj)
		}

		for (row in 1 until worksheet.sheetData.row.size) {
			for (column in worksheet.sheetData.row[row].c.indices) {
				table.columns[column].also {
					it.cells += extractValue(worksheet.sheetData.row[row].c[column])
				}
			}
		}
		table.isInitialized = true
	}

	fun getHeadersAndExamples(): List<List<String?>> {
		val headers: MutableList<String> = mutableListOf()
		val examples: MutableList<String> = mutableListOf()
		if (worksheet.sheetData.row[0].c.isEmpty()) throw NoSuchElementException("First row of table was empty")
		for (cell in worksheet.sheetData.row[0].c.indices) {
			headers.add(extractValue(worksheet.sheetData.row[0].c[cell]).getString())
		}
		if (worksheet.sheetData.row.count() > 1) {
			for (cell in worksheet.sheetData.row[1].c.indices) {
				examples.add(extractValue(worksheet.sheetData.row[1].c[cell]).getString())
			}
		}
		return listOf(headers, examples)
	}

	fun save(): ByteArray {
		val pkg = SpreadsheetMLPackage.createPackage()
		val factory = Context.getsmlObjectFactory()
		val sheet = pkg.createWorksheetPart(PartName("/xl/worksheets/sheet1.xml"), "result", 1)
		val sheetData = sheet.contents.sheetData
		val header = factory.createRow()
		for (columnNumber in 0 until table.columns.size) {
			val cell = table.headers[columnNumber].getXlsxRepresentation()
			cell.r = toExcel(columnNumber) + "1"
			header.c.add(cell)
		}
		sheetData.row.add(header)

		for (rowNumber in 1 until table.rowsCount + 1) {
			val row = factory.createRow()
			for (columnNumber in 0 until table.columns.size) {
				val cell = table.columns[columnNumber][rowNumber].getXlsxRepresentation()
				cell.r = toExcel(columnNumber) + (rowNumber + 1).toString()
				row.c.add(cell)
			}
			sheetData.row.add(row)
		}
		val stream = ByteArrayOutputStream()
		pkg.save(stream)
		return stream.toByteArray()
	}

	fun clean() {
		for (column in table.columns) {
			column.cells.clear()
		}
	}

	private fun extractValue(docx4jCell: org.xlsx4j.sml.Cell): SObject {
		return when (docx4jCell.t) {
			STCellType.B -> SBoolean(docx4jCell)
			STCellType.N -> SNumber(docx4jCell.v.toDouble(), (document.parts[stylesPart] as Styles).getXfByIndex(docx4jCell.s).numFmtId)
			STCellType.E -> SNull()
			STCellType.S -> SString(document, docx4jCell)
			STCellType.STR -> SNull()
			STCellType.INLINE_STR -> SString(document, docx4jCell)
			null -> SNull()
		}
	}

	companion object {
		private val sheet = PartName("/xl/worksheets/sheet1.xml")
		private val alphabet = ('A'..'Z').toMutableList()
		private val stylesPart = PartName("/xl/styles.xml")

		fun toExcel(number: Int): String {
			var num = number + 1
			fun divAndMod(n: Int): Pair<Int, Int> {
				val a = n / 26
				val b = n % 26
				return if (b == 0) Pair(a - 1, 26) else Pair(a, b)
			}

			val chars = mutableListOf<Char>()
			while (num > 0) {
				val param = divAndMod(num)
				num = param.first
				chars.add(alphabet[param.second - 1])
			}
			return chars.joinToString { it.toString() }
		}
	}
}