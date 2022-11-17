package com.maeasoftworks.alfaconverter.wrappers

import com.maeasoftworks.alfaconverter.model.datatypes.XObject

internal class Table {
	var isInitialized: Boolean = false
	val columns: MutableMap<Int, Column> = HashMap()

	val headers: MutableList<Cell> = ArrayList()

	private val scope = Scope(this)

	val rowsCount: Int
		get() = columns.values.maxOf { it.cells.size }

	operator fun get(column: Int, row: Int): Cell? {
		return columns[column]?.get(row)
	}

	operator fun get(column: Int): Column? {
		return columns[column]
	}

	operator fun get(columns: List<Int>): List<Column> {
		return columns.map { this.columns[it]!! }
	}

	operator fun set(column: Int, cell: Int, value: Cell) {
		columns[column]?.set(cell, value)
	}

	internal fun append(column: Int, row: Int, cell: Cell) {
		if (!columns.values.any { it.pos == column }) {
			columns[column] = Column(column)
		}
		if (columns[column]?.cells?.values?.any { it.row == row && it.column == column } == false) {
			columns[column]?.set(row, cell)
		}
	}

	fun fill(function: Scope.() -> Unit): Table {
		isInitialized = true
		scope.function()
		return this
	}

	companion object {
		internal fun slice(columns: List<Column>, pos: Int): List<Cell?> {
			return columns.map { it[pos] }
		}
	}

	inner class Scope(private val table: Table) {
		operator fun Column.unaryPlus() {
			table.columns[this.pos] = this
		}

		fun header(cell: Cell) {
			columns[cell.column] = Column(cell.column)
			headers.add(cell)
		}

		fun put(cell: Cell) {
			table.append(cell.column, cell.row, cell)
		}

		infix fun Cell.with(obj: XObject): Cell {
			this.value = obj
			return this
		}
	}
}