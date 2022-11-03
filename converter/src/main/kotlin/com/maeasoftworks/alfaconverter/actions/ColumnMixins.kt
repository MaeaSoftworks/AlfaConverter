package com.maeasoftworks.alfaconverter.actions

import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Column

fun List<Column>.horizontal(pos: Int) : List<Cell?> {
	return this.map { it[pos] }
}