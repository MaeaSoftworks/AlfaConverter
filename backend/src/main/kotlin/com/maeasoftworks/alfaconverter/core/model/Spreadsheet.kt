package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.conversions.Target
import com.maeasoftworks.alfaconverter.core.conversions.pos
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SBoolean
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SNull
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SNumber
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
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
		for (row in worksheet.sheetData.row.indices) {
			for (cell in worksheet.sheetData.row[row].c.indices) {
				cell.pos.let { table.append(it, row, extractValue(worksheet.sheetData.row[row].c[cell], row, it)) }
			}
		}
		extractHeaders(table)
		table.isInitialized = true
	}

	fun getHeadersAndExamples(): List<List<String?>> {
		val headers: MutableList<Cell> = mutableListOf()
		val examples: MutableList<Cell> = mutableListOf()
		if (worksheet.sheetData.row[0].c.isEmpty()) throw NoSuchElementException("First row of table was empty")
		for (column in worksheet.sheetData.row[0].c.indices) {
			headers.add(extractValue(worksheet.sheetData.row[0].c[column], 0, column.pos))
		}
		if (worksheet.sheetData.row.count() > 1) {
			for (column in worksheet.sheetData.row[1].c.indices) {
				examples.add(extractValue(worksheet.sheetData.row[1].c[column], 1, column.pos))
			}
		}
		return listOf(headers.map { it.value.getString() }, examples.map { it.value.getString() })
	}

	fun save(): ByteArray {
		val pkg = SpreadsheetMLPackage.createPackage()
		val factory = Context.getsmlObjectFactory()
		val sheet = pkg.createWorksheetPart(PartName("/xl/worksheets/sheet1.xml"), "result", 1)
		val sheetData = sheet.contents.sheetData
		val header = factory.createRow()
		for (columnNumber in 0 until table.columns.size) {
			val cell = table.headers[columnNumber].value.getXlsxRepresentation()
			cell.r = toExcel(columnNumber) + "1"
			header.c.add(cell)
		}
		sheetData.row.add(header)

		for (rowNumber in 1 until table.rowsCount + 1) {
			val row = factory.createRow()
			for (columnNumber in 0 until table.columns.size) {
				val cell = table.columns[columnNumber.pos]?.get(rowNumber)?.value?.getXlsxRepresentation() ?: factory.createCell()
				cell?.r = toExcel(columnNumber) + (rowNumber + 1).toString()
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
			column.value.cells.clear()
		}
	}

	private fun extractHeaders(table: Table): Table {
		table.columns.forEach { (key, value) ->
			table.headers.add(value.cells.values.first())
			table.columns[key]!!.cells.remove(0)
		}
		return table
	}

	private fun extractValue(docx4jCell: org.xlsx4j.sml.Cell, row: Int, column: Target): Cell {
		val cell = Cell(column, row)
		when (docx4jCell.t) {
			STCellType.B -> {
				cell.value = SBoolean(docx4jCell)
			}

			STCellType.N -> {
				cell.value = SNumber(
					docx4jCell.v.toDouble(),
					(document.parts[stylesPart] as Styles).getXfByIndex(docx4jCell.s).numFmtId
				)
			}

			STCellType.E -> {
				cell.value = SNull()
			}

			STCellType.S -> {
				cell.value = SString(document, docx4jCell)
			}

			STCellType.STR -> {
				cell.value = SNull()
			}

			STCellType.INLINE_STR -> {
				cell.value = SString(document, docx4jCell)
			}

			null -> {
				cell.value = SNull()
			}
		}
		return cell
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