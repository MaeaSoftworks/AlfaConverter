package com.maeasoftworks.alfaconverter.wrappers

import com.maeasoftworks.alfaconverter.conversions.TypeConversion
import com.maeasoftworks.alfaconverter.model.datatypes.XFactory

internal class Column(val pos: Int) {
	lateinit var name: String
	val cells: MutableMap<Int, Cell> = HashMap()
	var hasAction: Boolean = false

	operator fun get(pos: Int): Cell? {
		return cells[pos]
	}

	operator fun set(cell: Int, value: Cell) {
		cells[cell] = value
	}

	fun changeType(conversion: TypeConversion) {
		cells.forEach { (_, cell) ->
			cell.value = XFactory.create(conversion, cell.value)
		}
	}
}