package com.maeasoftworks.alfaconverter.wrappers

internal class Cell(
	var row: Int,
	var column: Int
) {
	var value: Any? = null
	var stringValue: String? = null
	lateinit var wrapped: org.xlsx4j.sml.Cell
	var format: DataFormat = DataFormat.STRING

	override fun equals(other: Any?): Boolean {
		return other != null
				&& other is Cell
				&& row == other.row
				&& column == other.column
				&& value == other.value
				&& stringValue == other.stringValue
	}

	override fun hashCode(): Int {
		var result = value?.hashCode() ?: 0
		result = 31 * result + wrapped.hashCode()
		return result
	}
}