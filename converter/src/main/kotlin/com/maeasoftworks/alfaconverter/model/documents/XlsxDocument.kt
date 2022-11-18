package com.maeasoftworks.alfaconverter.model.documents

import com.maeasoftworks.alfaconverter.model.datatypes.XBoolean
import com.maeasoftworks.alfaconverter.model.datatypes.XNull
import com.maeasoftworks.alfaconverter.model.datatypes.XNumber
import com.maeasoftworks.alfaconverter.model.datatypes.XString
import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.Styles
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import org.xlsx4j.jaxb.Context
import org.xlsx4j.sml.STCellType
import org.xlsx4j.sml.Worksheet
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class XlsxDocument : Document() {
	private lateinit var document: SpreadsheetMLPackage
	private lateinit var worksheet: Worksheet

	override fun open(file: ByteArray): Document {
		document = SpreadsheetMLPackage.load(ByteArrayInputStream(file))
		worksheet = (document.parts[sheet] as WorksheetPart).jaxbElement
		return this
	}

	override fun initializeTable() {
		table = Table()
		for (row in worksheet.sheetData.row.indices) {
			for (cell in worksheet.sheetData.row[row].c.indices) {
				table.append(cell, row, extractValue(worksheet.sheetData.row[row].c[cell], row, cell))
			}
		}
		extractHeaders(table)
	}

	override fun getHeadersAndExamples(): List<List<String?>> {
		val headers: MutableList<Cell> = mutableListOf()
		val examples: MutableList<Cell> = mutableListOf()
		if (worksheet.sheetData.row[0].c.isEmpty()) throw NoSuchElementException("First row of table was empty")
		for (column in worksheet.sheetData.row[0].c.indices) {
			headers.add(extractValue(worksheet.sheetData.row[0].c[column], 0, column))
		}
		if (worksheet.sheetData.row.count() > 1) {
			for (column in worksheet.sheetData.row[1].c.indices) {
				examples.add(extractValue(worksheet.sheetData.row[1].c[column], 1, column))
			}
		}
		return listOf(headers.map { it.value.getString() }, examples.map { it.value.getString() })
	}

	override fun save(): ByteArray {
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
				val cell = table.columns[columnNumber]?.get(rowNumber)?.value?.getXlsxRepresentation()
				cell?.r = toExcel(columnNumber) + (rowNumber + 1).toString()
				row.c.add(cell)
			}
			sheetData.row.add(row)
		}
		val stream = ByteArrayOutputStream()
		pkg.save(stream)
		return stream.toByteArray()
	}

	private fun extractHeaders(table: Table): Table {
		table.columns.forEach { (key, value) ->
			table.headers.add(value.cells.values.first())
			table.columns[key]!!.cells.remove(0)
		}
		return table
	}

	private fun extractValue(docx4jCell: org.xlsx4j.sml.Cell, row: Int, column: Int): Cell {
		val cell = Cell(row, column)
		when (docx4jCell.t) {
			STCellType.B -> {
				cell.value = XBoolean(docx4jCell)
			}

			STCellType.N -> {
				cell.value = XNumber(
					docx4jCell.v.toDouble(),
					(document.parts[stylesPart] as Styles).getXfByIndex(docx4jCell.s).numFmtId
				)
			}

			STCellType.E -> {
				cell.value = XNull()
			}

			STCellType.S -> {
				cell.value = XString(document, docx4jCell)
			}

			STCellType.STR -> {
				cell.value = XNull()
			}

			STCellType.INLINE_STR -> {
				cell.value = XString(document, docx4jCell)
			}

			null -> {
				cell.value = XNull()
			}
		}
		return cell
	}

	companion object {
		private val sheet = PartName("/xl/worksheets/sheet1.xml")
		private val alphabet = ('A'..'Z').toMutableList()
		private val stylesPart = PartName("/xl/styles.xml")

		fun toExcel(number: Int): String {
			var num = number
			fun divAndMod(n: Int): Pair<Int, Int> {
				val a = n / 26
				val b = n % 26
				return if (b == 0) Pair(a - 1, 26) else Pair(a, b)
			}

			val chars = mutableListOf<Char>()
			while (num > 0) {
				val param = divAndMod(num)
				num = param.first
				chars.add(alphabet[param.second])
			}
			return chars.joinToString { it.toString() }
		}
	}
}