package com.maeasoftworks.alfaconverter.wrappers

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.xlsx4j.sml.Worksheet

class Table {
	val columns: MutableMap<Int, Column> = HashMap()

	val rowsCount: Int
		get() = columns.values.maxOf { it.cells.size }

	operator fun get(column: Int, row: Int) : Cell? {
		return columns[column]?.get(row)
	}

	operator fun get(column: Int) : Column? {
		return columns[column]
	}

	operator fun get(columns: List<Int>) : List<Column> {
		return columns.map { this.columns[it]!! }
	}

	operator fun set(column: Int, cell: Int, value: Cell) {
		columns[column]?.set(cell, value)
	}

	private fun append(column: Int, row: Int, value: org.xlsx4j.sml.Cell, spreadsheet: SpreadsheetMLPackage) {
		if (columns.values.all {it.pos != column}) {
			columns[column] = Column(column)
		}
		if (columns[column]?.cells?.values?.all { it.row != row } == true) {
			columns[column]?.set(row, Cell(column, row))
		}
		columns[column]?.get(row)?.value = Cell.extractValue(value, spreadsheet)
	}

	companion object {
		fun create(spreadsheet: SpreadsheetMLPackage, worksheet: Worksheet) : Table {
			val table = Table()
			for (row in worksheet.sheetData.row.indices) {
				for (cell in worksheet.sheetData.row[row].c.indices) {
					table.append(cell, row, worksheet.sheetData.row[row].c[cell], spreadsheet)
				}
			}
			return table
		}
	}
}