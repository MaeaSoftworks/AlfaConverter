package com.maeasoftworks.alfaconverter.wrappers

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.xlsx4j.sml.Worksheet

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

	companion object {
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
	}
}