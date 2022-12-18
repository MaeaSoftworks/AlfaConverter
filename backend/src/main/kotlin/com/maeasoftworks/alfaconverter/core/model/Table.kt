package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.xlsx.structure.Data

/**
 * Data store. All formats can be represented as table and all conversions also can be performed on tables.
 */
class Table {
	/**
	 * List of columns.
	 */
	val columns: MutableList<Column> = mutableListOf()

	/**
	 * List of column addresses, a.k.a headers.
	 */
	val headers: MutableList<ColumnAddress> = mutableListOf()

	/**
	 * Count of rows in table excluding header.
	 */
	val rowsCount: Int
		get() = columns.maxOf { it.cells.size }

	/**
	 * Get value by column address & row index.
	 * @param column column address.
	 * @param row row index.
	 * @return value of cell.
	 */
	operator fun get(column: ColumnAddress, row: Int): Data? {
		return columns.firstOrNull { it.name == column }?.get(row)
	}

	/**
	 * Get column by column address.
	 * @param column column address.
	 * @return column.
	 */
	operator fun get(column: ColumnAddress): Column? {
		return columns.firstOrNull { it.name == column }
	}

	/**
	 * Get list of columns by column addresses.
	 * @param columns column addresses.
	 * @return list of columns.
	 */
	operator fun get(columns: List<ColumnAddress>): List<Column> {
		return this.columns.filter { it.name in columns }
	}

	/**
	 * Safely set value to cell.
	 * @param column column address.
	 * @param row row index.
	 */
	operator fun set(column: ColumnAddress, row: Int, value: Data) {
		columns.firstOrNull { it.name == column }?.let {
			if (it.cells.size == row) {
				it.cells.add(value)
			} else if (it.cells.size > row) {
				it.cells[row] = value
			} else {
				throw IndexOutOfBoundsException()
			}
		}
	}

	companion object {
		/**
		 * Provides row-based access to table or specified columns.
		 * @param columns columns that need to be viewed.
		 * @param pos row position to view.
		 * @return list of values in [pos] row of table/columns.
		 */
		fun slice(columns: List<Column>, pos: Int): List<Data?> {
			return columns.map { it[pos] }
		}
	}

	/**
	 * Data store for one column of table.
	 */
	open class Column(var name: ColumnAddress) {
		/**
		 * Data values.
		 */
		val cells: MutableList<Data> = mutableListOf()

		/**
		 * Get value directly from column without accessing [cells].
		 * @param pos row index.
		 * @return value of cell.
		 */
		operator fun get(pos: Int): Data? {
			return if (pos < cells.size) cells[pos] else null
		}
	}
}