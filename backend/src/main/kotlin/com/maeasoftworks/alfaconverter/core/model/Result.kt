package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.Converter

/**
 * Abstract representation of result file.
 */
interface Result {
    /**
     * Result table.
     */
    var table: Table

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
