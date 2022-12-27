package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.Table.*
import com.maeasoftworks.alfaconverter.core.xlsx.NumberData
import com.maeasoftworks.alfaconverter.core.xlsx.StringData
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Holds all actions that will be applied to source table.
 * @param actions actions that will be applied to source table.
 * @constructor Creates a new instance.
 */
@Serializable
data class Conversion(val actions: @Contextual MutableList<Action> = mutableListOf())

/**
 * Base type for all actions.
 */
@Serializable
sealed class Action {
    /**
     * Action body. All doings must get data from [initialTable], handle it and put into [resultTable].
     * @param initialTable table from [Source]
     * @param resultTable table from [Result]
     */
    abstract fun run(initialTable: Table, resultTable: Table)
}

/**
 * Action that moves all data from [initialColumn] to [targetColumn].
 * @param initialColumn address of column in [Source] table that will be bound.
 * @param targetColumn address of column in [Result] table that will be filled.
 * @constructor Creates new instance.
 */
@Serializable
@SerialName("bind")
class Bind(private val initialColumn: ColumnAddress, private val targetColumn: ColumnAddress) : Action() {
    override fun run(initialTable: Table, resultTable: Table) = initialTable[initialColumn].forEach { resultTable[targetColumn].add(it) }
}

/**
 * Action that cast all data from in [targetColumn] to specified [dataFormat] of [NumberData].
 *
 * Applicable only for [Xlsx] result implementation.
 * @param targetColumn address of column in [Result] table that will be cast.
 * @param dataFormat [NumberData] format.
 * @constructor Creates new instance.
 */
@Suppress("unused")
@Serializable
@SerialName("cast")
class Cast(private val targetColumn: ColumnAddress, private val dataFormat: Long? = 0) : Action() {
    override fun run(initialTable: Table, resultTable: Table) {
        resultTable[targetColumn].let {
            for (cell in it.indices) {
                it[cell] = NumberData(it[cell]!!.getString().toDouble(), dataFormat!!)
            }
        }
    }
}

/**
 * Action that merges all data from [initialColumns] to [targetColumn] by [pattern].
 * @param initialColumns addresses of columns in [Source] table that will be merged.
 * @param targetColumn address of column in [Result] table that will be filled.
 * @param pattern merging pattern. insertion template format: "${_&lt;address1&gt;_}".
 * @constructor Creates new instance.
 */
@Serializable
@SerialName("merge")
class Merge(private val initialColumns: List<ColumnAddress>, private val targetColumn: ColumnAddress, private val pattern: String) : Action() {
    override fun run(initialTable: Table, resultTable: Table) {
        val sourceColumns = initialTable[initialColumns]
        for (y in 0 until initialTable.rowsCount) {
            var result = pattern
            var pos = 0
            Table.slice(sourceColumns, y).forEach { cell ->
                result = result.replace("\${${pos++}}", cell?.getString() ?: "")
            }
            resultTable[targetColumn, y] = StringData(result)
        }
    }
}

/**
 * Action that splits all data from [initialColumn] to [targetColumns].
 * @param initialColumn address of column in [Source] table that will be split.
 * @param targetColumns addresses of columns in [Result] table that will be filled.
 * @param pattern regex pattern.
 * @constructor Creates new instance.
 */
@Serializable
@SerialName("split")
class Split(private val initialColumn: ColumnAddress, private val targetColumns: List<ColumnAddress>, private val pattern: String) : Action() {
    @Transient
    private val regex = Regex(pattern)

    override fun run(initialTable: Table, resultTable: Table) {
        val initialColumn = initialTable[initialColumn]
        for (row in 0 until initialTable.rowsCount) {
            val results = initialColumn.getOrNull(row)?.getString()?.let { regex.matchEntire(it)?.groups?.filterNotNull()?.drop(1) }
            if (results == null) {
                for (column in targetColumns.indices) {
                    resultTable[targetColumns[column], row] = null
                }
            } else {
                for (column in results.indices) {
                    resultTable[targetColumns[column], row] = StringData(results[column].value)
                }
            }
        }
    }
}