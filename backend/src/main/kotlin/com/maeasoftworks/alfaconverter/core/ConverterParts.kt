package com.maeasoftworks.alfaconverter.core

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

/**
 * Abstract representation of modifier file.
 */
interface Modifier {
    /**
     * Retrieves all addresses from table.
     * @return list of addresses.
     */
    fun getHeaders(): List<ColumnAddress>

    /**
     * Retrieves payload (type-specific).
     * @return payload.
     */
    fun getPayload(): Any?
}

/**
 * Abstract representation of result file.
 */
interface Result {
    /**
     * Result table.
     */
    val table: Table

    /**
     * Used to complete initialization. Will be called in [Converter] init block.
     * @param parent [Converter] that owns this [Result].
     */
    fun initialize(parent: Converter<*, *, *>)

    /**
     * Data saving.
     * @return data in [ByteArray] representation.
     */
    fun convert(): ByteArray
}
