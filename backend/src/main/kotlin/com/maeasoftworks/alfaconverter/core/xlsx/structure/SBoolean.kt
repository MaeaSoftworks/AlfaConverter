package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType

class SBoolean : SObject {
	private var value: Boolean = false

	constructor(value: Boolean) {
		this.value = value
	}

	constructor(cell: Cell) {
		value = cell.v == "1"
	}

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

	override fun equals(other: Any?): Boolean {
		return other != null
				&& other is SBoolean
				&& value == other.value
	}

	override fun hashCode(): Int {
		return value.hashCode() * 17
	}
}