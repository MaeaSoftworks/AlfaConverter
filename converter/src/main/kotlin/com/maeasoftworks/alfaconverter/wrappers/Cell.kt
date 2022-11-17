package com.maeasoftworks.alfaconverter.wrappers

import com.maeasoftworks.alfaconverter.model.datatypes.XObject

internal class Cell(
	var row: Int,
	var column: Int
) {
	lateinit var value: XObject

	override fun equals(other: Any?): Boolean {
		return other != null
				&& other is Cell
				&& row == other.row
				&& column == other.column
				&& value == other.value
	}

	override fun hashCode(): Int {
		var result = row
		result = 31 * result + column
		result = 31 * result + value.hashCode()
		return result
	}
}