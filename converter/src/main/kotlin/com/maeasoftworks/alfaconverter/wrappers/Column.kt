package com.maeasoftworks.alfaconverter.wrappers

class Column(val pos: Int) {
	lateinit var name: String
	val cells: MutableMap<Int, Cell> = HashMap()
	var hasAction: Boolean = false

	operator fun get(pos: Int) : Cell? {
		return cells[pos]
	}

	operator fun set(cell: Int, value: Cell) {
		cells[cell] = value
	}
}