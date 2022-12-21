package com.maeasoftworks.alfaconverter.core.model

/**
 * Abstract representation of source file.
 */
interface Source {
    /**
     * Source table.
     */
    val table: Table

    /**
     * Retrieves all addresses from table.
     * @return list of addresses.
     */
    fun getHeaders(): List<ColumnAddress>

    /**
     * Retrieves data from first line of table.
     * @return list of serialized values.
     */
    fun getExamples(): List<String>
}
