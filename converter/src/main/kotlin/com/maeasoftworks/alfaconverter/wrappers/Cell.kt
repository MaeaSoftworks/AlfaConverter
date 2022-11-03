package com.maeasoftworks.alfaconverter.wrappers

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings
import org.xlsx4j.sml.STCellType

class Cell(
	var row: Int,
	var column: Int
) {
	var value: String? = null

	companion object {
		private val sharedStringsPart = PartName("/xl/sharedStrings.xml")

		fun extractValue(cell: org.xlsx4j.sml.Cell, spreadsheet: SpreadsheetMLPackage): String {
			return when (cell.t) {
				STCellType.B -> "boolean"
				STCellType.N -> "n"
				STCellType.E -> "error"
				STCellType.S -> findString(spreadsheet, cell.v)
				STCellType.STR -> ""
				STCellType.INLINE_STR -> "Unit"
				null -> "null"
			}
		}

		private fun findString(spreadsheet: SpreadsheetMLPackage, link: String): String {
			val strings = spreadsheet.parts[sharedStringsPart] as SharedStrings
			return strings.jaxbElement.si[link.toInt()].t.value
		}
	}
}