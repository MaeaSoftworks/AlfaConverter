package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.xlsx.Data

/**
 * Data store for one column of table.
 */
typealias Column = MutableList<Data?>

/**
 * Address of column in table.
 */
typealias ColumnAddress = List<String>

/**
 * [IndexOutOfBoundsException]-safe getter implementation.
 * @param pos position of object.
 * @return object or null if position was incorrect.
 */
fun Column.getOrNull(pos: Int) = if (size <= pos || size == 0) null else this[pos]

/**
 * Data store. All formats can be represented as table and all conversions also can be performed on tables.
 */
class Table {
    constructor()

    constructor(vararg addresses: ColumnAddress) {
        addresses.forEach { add(it) }
    }

    constructor(vararg column: Pair<ColumnAddress, Column>) {
        column.forEach {
            add(it.first)
            values[it.first] = it.second
        }
    }

    /**
     * List of columns.
     */
    val values: LinkedHashMap<ColumnAddress, Column> = linkedMapOf()

    /**
     * Shortcut for table first row a.k.a headers.
     */
    val headers: List<ColumnAddress>
        get() = values.keys.toList()

    /**
     * Shortcut for table data.
     */
    val columns: List<Column>
        get() = values.values.toList()

    /**
     * Count of rows in table excluding header.
     */
    val rowsCount: Int
        get() = values.values.maxOf { it.size }

    /**
     * Get value by column address & row index.
     * @param column column address.
     * @param row row index.
     * @return value of cell.
     */
    operator fun get(column: ColumnAddress, row: Int): Data? {
        return values[column]?.let { if (it.size <= row) null else it[row] }
    }

    /**
     * Get column by column address.
     * @param column column address.
     * @return column.
     */
    @JvmName("getColumn")
    operator fun get(column: ColumnAddress): Column {
        return values[column] ?: throw IndexOutOfBoundsException("Column $column not found")
    }

    /**
     * Get list of columns by column addresses.
     * @param columns column addresses.
     * @return list of columns.
     */
    @JvmName("getColumns")
    operator fun get(columns: List<ColumnAddress>): List<Column> {
        return columns.map { this.values[it] ?: throw IndexOutOfBoundsException("Column $it not found") }
    }

    /**
     * Get column by column position.
     * @param column column position.
     * @return list of columns.
     */
    operator fun get(column: Int): Column {
        return values[values.keys.toList()[column]] ?: throw IndexOutOfBoundsException("Column $column not found")
    }

    /**
     * Safely set value to cell.
     * @param column column address.
     * @param row row index.
     */
    operator fun set(column: ColumnAddress, row: Int, value: Data?) {
        values[column]?.let {
            if (it.size == row) {
                it.add(value)
            } else if (it.size > row) {
                it[row] = value
            } else {
                throw IndexOutOfBoundsException("Column $column not found")
            }
        }
    }

    /**
     * Create empty column with header.
     * @param address column address/header.
     */
    fun add(address: ColumnAddress) {
        values[address] = mutableListOf()
    }

    companion object {
        /**
         * Provides row-based access to specified columns.
         * @param columns columns that need to be viewed.
         * @param pos row position to view.
         * @return list of values in [pos] row of columns.
         */
        fun slice(columns: List<Column>, pos: Int): List<Data?> {
            return columns.map { it.getOrNull(pos) }
        }

        /**
         * Provides row-based access to table.
         * @param table table that need to be viewed.
         * @param pos row position to view.
         * @return list of values in [pos] row of table.
         */
        fun slice(table: Table, pos: Int): List<Data?> {
            return table.values.values.map { it[pos] }
        }
    }
}
