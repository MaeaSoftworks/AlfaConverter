package com.maeasoftworks.alfaconverter.wrappers

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.xlsx4j.jaxb.Context
import org.xlsx4j.sml.CTRst
import org.xlsx4j.sml.ObjectFactory
import org.xlsx4j.sml.STCellType
import org.xlsx4j.sml.Worksheet
import java.io.ByteArrayOutputStream


internal class Table {
	val columns: MutableMap<Int, Column> = HashMap()

	val headers: MutableList<Cell> = ArrayList()

	val rowsCount: Int
		get() = columns.values.maxOf { it.cells.size }

	operator fun get(column: Int, row: Int): Cell? {
		return columns[column]?.get(row)
	}

	operator fun get(column: Int): Column? {
		return columns[column]
	}

	operator fun get(columns: List<Int>): List<Column> {
		return columns.map { this.columns[it]!! }
	}

	operator fun set(column: Int, cell: Int, value: Cell) {
		columns[column]?.set(cell, value)
	}

	private fun append(column: Int, row: Int, value: org.xlsx4j.sml.Cell, spreadsheet: SpreadsheetMLPackage) {
		if (!columns.values.any { it.pos == column }) {
			columns[column] = Column(column)
		}
		if (columns[column]?.cells?.values?.any { it.row == row && it.column == column } == false) {
			columns[column]?.set(row, Cell.extractValue(value, spreadsheet, row, column))
		}
	}

	fun save(): ByteArray {
		val pkg = SpreadsheetMLPackage.createPackage()
		val factory = Context.getsmlObjectFactory()

		val sheet = pkg.createWorksheetPart(PartName("/xl/worksheets/sheet1.xml"), "result", 1)
		val sheetData = sheet.contents.sheetData

		val header = factory.createRow()
		for (columnNumber in 0 until columns.size) {
			val cell = createCell(headers[columnNumber].stringValue, factory)
			cell.r = toExcel(columnNumber) + "1"
			header.c.add(cell)
		}
		sheetData.row.add(header)

		for (rowNumber in 1 until rowsCount) {
			val row = factory.createRow()
			for (columnNumber in 0 until columns.size) {
				val cell = createCell(columns[columnNumber]?.get(rowNumber)?.stringValue, factory)
				cell.r = toExcel(columnNumber) + (rowNumber + 1).toString()
				row.c.add(cell)
			}
			sheetData.row.add(row)
		}
		val stream = ByteArrayOutputStream()
		pkg.save(stream)
		return stream.toByteArray()
	}


	private fun createCell(content: String?, factory: ObjectFactory): org.xlsx4j.sml.Cell {
		val cell = factory.createCell()
		val ctx = factory.createCTXstringWhitespace()
		ctx.value = content
		val ctr = CTRst()
		ctr.t = ctx
		cell.t = STCellType.INLINE_STR
		cell.`is` = ctr
		return cell
	}

	companion object {
		private val alphabet = ('A'..'Z').toMutableList()

		fun create(spreadsheet: SpreadsheetMLPackage, worksheet: Worksheet): Table {
			val table = Table()
			for (row in worksheet.sheetData.row.indices) {
				for (cell in worksheet.sheetData.row[row].c.indices) {
					table.append(cell, row, worksheet.sheetData.row[row].c[cell], spreadsheet)
				}
			}
			extractHeaders(table)
			return table
		}

		fun getHeaders(spreadsheet: SpreadsheetMLPackage, worksheet: Worksheet): List<String?> {
			val headers: MutableList<Cell> = mutableListOf()
			if (worksheet.sheetData.row[0].c.isEmpty()) throw NoSuchElementException("First row of table was empty")
			for (column in worksheet.sheetData.row[0].c.indices) {
				headers.add(Cell.extractValue(worksheet.sheetData.row[0].c[column], spreadsheet, 0, column))
			}
			return headers.map { it.stringValue }
		}

		private fun extractHeaders(table: Table): Table {
			table.columns.forEach { (key, value) ->
				table.headers.add(value.cells.values.first())
				table.columns[key]!!.cells.remove(0)
			}
			return table
		}

		internal fun slice(columns: List<Column>, pos: Int): List<Cell?> {
			return columns.map { it[pos] }
		}

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