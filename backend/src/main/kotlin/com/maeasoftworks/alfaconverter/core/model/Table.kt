package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.xlsx.structure.Data

class Table {
	val columns: MutableList<Column> = mutableListOf()

	val headers: MutableList<ColumnAddress> = mutableListOf()

	val rowsCount: Int
		get() = columns.maxOf { it.cells.size }

	operator fun get(column: ColumnAddress, row: Int): Data? {
		return columns.firstOrNull { it.name == column }?.get(row)
	}

	operator fun get(column: ColumnAddress): Column? {
		return columns.firstOrNull { it.name == column }
	}

	operator fun get(columns: List<ColumnAddress>): List<Column> {
		return this.columns.filter { it.name in columns }
	}

	operator fun set(column: ColumnAddress, row: Int, value: Data) {
		columns.firstOrNull { it.name == column }?.let {
			if (it.cells.size == row) {
				it.cells.add(value)
			} else if (it.cells.size > row) {
				it.set(row, value)
			} else {
				throw IndexOutOfBoundsException()
			}
		}
	}

	internal fun append(column: ColumnAddress, row: Int, value: Data) {
		if (!columns.any { it.name == column }) {
			columns += Column(column)
		}
		if (columns.firstOrNull { it.name == column }?.cells?.any { it == value } == false) {
			columns.first { it.name == column }[row] = value
		}
	}

	fun fill(function: Builder.() -> Unit): Table {
		this.Builder().function()
		return this
	}

	companion object {
		internal fun slice(columns: List<Column>, pos: Int): List<Data?> {
			return columns.map { it[pos] }
		}
	}

	inner class Builder {
		fun column(name: ColumnAddress, function: (Builder.() -> Unit)? = null) {
			columns += Column(name)
			headers += name
			function?.invoke(this)
		}

		operator fun Data.unaryPlus() {
			columns.last().cells += this
		}
	}

	open class Column(var name: ColumnAddress) {
		val cells: MutableList<Data> = mutableListOf()

		operator fun get(pos: Int): Data? {
			return if (pos < cells.size) cells[pos] else null
		}

		operator fun set(cell: Int, value: Data) {
			cells[cell] = value
		}
	}
}