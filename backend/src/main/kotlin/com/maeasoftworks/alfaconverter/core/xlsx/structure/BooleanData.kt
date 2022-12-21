package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType

data class BooleanData(private var value: Boolean = false) : Data() {

	constructor(cell: Cell) : this(cell.v == "1")

	override fun getXlsxRepresentation(): Cell {
		return Cell().also {
			it.t = STCellType.B
			it.v = if (value) "1" else "0"
		}
	}

	override fun getJsonRepresentation(): String {
		return value.toString()
	}

	override fun getXmlRepresentation(): String {
		return value.toString()
	}

	override fun getString(): String {
		return value.toString()
	}
}