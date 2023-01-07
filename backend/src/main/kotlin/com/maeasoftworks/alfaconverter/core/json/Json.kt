package com.maeasoftworks.alfaconverter.core.json

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.Result
import com.maeasoftworks.alfaconverter.core.Source
import com.maeasoftworks.alfaconverter.core.Table
import com.maeasoftworks.alfaconverter.modules.serializer
import kotlinx.serialization.encodeToString

/**
 * Implementation of [Result] for json result type.
 */
class Json : Result {
    override var table: Table = Table()

    /**
     * Gets [Source] table and set it to local table.
     * @param parent [Converter] that owns this [Result].
     */
    override fun initialize(parent: Converter<*, *, *>) {
        table = parent.source.table
    }

    override fun convert(): ByteArray {
        val result = mutableListOf<Map<String, String?>>()
        val headers = table.headers.map { it[0] }
        for (i in 0 until table.rowsCount) {
            val record = Table.slice(table, i).map { it?.asJsonValue() }
            result.add(headers.zip(record).toMap())
        }
        return serializer.encodeToString(result).toByteArray()
    }
}
