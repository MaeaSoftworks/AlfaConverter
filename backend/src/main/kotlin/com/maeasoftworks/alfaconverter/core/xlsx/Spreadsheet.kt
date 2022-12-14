package com.maeasoftworks.alfaconverter.core.xlsx

import com.maeasoftworks.alfaconverter.core.model.ColumnAddress
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.Column
import com.maeasoftworks.alfaconverter.core.xlsx.structure.*
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
	var table: Table

	constructor(file: ByteArray) {
		document = SpreadsheetMLPackage.load(ByteArrayInputStream(file))
		worksheet = (document.parts[sheet] as WorksheetPart).jaxbElement
		table = Table()
		for (columnPos in worksheet.sheetData.row[0].c.indices) {
			val sObj = extractValue(worksheet.sheetData.row[0].c[columnPos])
			val column = Column(listOf(sObj.getString().trim()))
			table.columns.add(column)
			table.headers.add(column.name)
		}

		for (row in 1 until worksheet.sheetData.row.size) {
			for (column in worksheet.sheetData.row[row].c.indices) {
				table.columns[column].also {
					it.cells += extractValue(worksheet.sheetData.row[row].c[column])
				}
			}
		}
	}

	constructor(table: Table) {
		this.table = table
	}

	fun getHeaders(): List<ColumnAddress> {
		val headers: MutableList<String> = mutableListOf()
		if (worksheet.sheetData.row[0].c.isEmpty()) throw NoSuchElementException("First row of table was empty")
		for (cell in worksheet.sheetData.row[0].c.indices) {
			headers.add(extractValue(worksheet.sheetData.row[0].c[cell]).getString())
		}
		return headers.map { listOf(it) }
	}

	fun getExamples(): List<String> {
		val examples: MutableList<String> = mutableListOf()
		if (worksheet.sheetData.row.count() > 1) {
			for (cell in worksheet.sheetData.row[1].c.indices) {
				examples.add(extractValue(worksheet.sheetData.row[1].c[cell]).getString())
			}
		}
		return examples
	}

	fun save(): ByteArray {
		val pkg = SpreadsheetMLPackage.createPackage()
		val factory = Context.getsmlObjectFactory()
		val sheet = pkg.createWorksheetPart(PartName("/xl/worksheets/sheet1.xml"), "result", 1)
		val sheetData = sheet.contents.sheetData
		val header = factory.createRow()
		for (columnNumber in 0 until table.columns.size) {
			val cell = StringData(table.headers[columnNumber][0]).getXlsxRepresentation()
			cell.r = toExcel(columnNumber) + "1"
			header.c.add(cell)
		}
		sheetData.row.add(header)

		for (rowNumber in 0 until table.rowsCount) {
			val row = factory.createRow()
			for (columnNumber in 0 until table.columns.size) {
				val cell = table.columns[columnNumber][rowNumber]?.getXlsxRepresentation()
				cell?.r = toExcel(columnNumber) + (rowNumber + 2).toString()
				if (cell != null) {
					row.c.add(cell)
				}
			}
			sheetData.row.add(row)
		}
		val stream = ByteArrayOutputStream()
		pkg.save(stream)
		return stream.toByteArray()
	}

	private fun extractValue(docx4jCell: org.xlsx4j.sml.Cell): Data {
		return when (docx4jCell.t) {
			STCellType.B -> BooleanData(docx4jCell)
			STCellType.N -> NumberData(
				docx4jCell.v.toDouble(),
				(document.parts[stylesPart] as Styles).getXfByIndex(docx4jCell.s).numFmtId
			)

			STCellType.E -> NullData()
			STCellType.S -> StringData(document, docx4jCell)
			STCellType.STR -> NullData()
			STCellType.INLINE_STR -> StringData(document, docx4jCell)
			null -> NullData()
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