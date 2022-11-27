package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.conversions.TypeConversion
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SFactory
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SObject

class Table {
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

		infix fun Cell.with(obj: SObject): Cell {
			this.value = obj
			return this
		}
	}

	class Column(val pos: Int) {
		val cells: MutableMap<Int, Cell> = HashMap()
		var hasAction: Boolean = false

		operator fun get(pos: Int): Cell? {
			return cells[pos]
		}

		operator fun set(cell: Int, value: Cell) {
			cells[cell] = value
		}

		fun changeType(conversion: TypeConversion) {
			cells.forEach { (_, cell) -> cell.value = SFactory.create(conversion, cell.value) }
		}
	}

	class Cell(
		var row: Int,
		var column: Int
	) {
		lateinit var value: SObject

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
}