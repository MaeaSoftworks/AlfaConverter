package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings
import org.xlsx4j.sml.CTRst
import org.xlsx4j.sml.CTXstringWhitespace
import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType

class SString : SObject {
	private lateinit var sharedStrings: SharedStrings
	private val value: String

	constructor(spreadsheet: SpreadsheetMLPackage, cell: Cell) {
		sharedStrings = spreadsheet.parts[sharedStringsPart] as SharedStrings
		value = sharedStrings.jaxbElement.si[cell.v.toInt()].t.value
	}

	constructor(value: String) {
		this.value = value
	}

	override fun getXlsxRepresentation(): Cell {
		return Cell().also {
			it.t = STCellType.INLINE_STR
			it.`is` = CTRst().also { ctr ->
				ctr.t = CTXstringWhitespace().also { ctx ->
					ctx.value = value
				}
			}
		}
	}

	override fun getJsonRepresentation(): String {
		return value
	}

	override fun getXmlRepresentation(): String {
		return "\"$value\""
	}

	override fun getString(): String {
		return value
	}

	override fun equals(other: Any?): Boolean {
		return other != null && other is SString && value == other.value
	}

	override fun hashCode(): Int {
		return value.hashCode() * 19
	}

	companion object {
		private val sharedStringsPart = PartName("/xl/sharedStrings.xml")
	}
}