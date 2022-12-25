package com.maeasoftworks.alfaconverter.core.xlsx

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings
import org.xlsx4j.sml.CTRst
import org.xlsx4j.sml.CTXstringWhitespace
import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

/**
 * Ancestor for all xlsx types.
 */
abstract class Data {
    /**
     * Get data represented as xlsx cell.
     * @return xlsx cell.
     */
    abstract fun asCell(): Cell

    /**
     * Get data represented as json value.
     * @return json value.
     */
    abstract fun asJsonValue(): String

    /**
     * Get data represented as xml value.
     * @return xml value.
     */
    abstract fun asXmlValue(): String

    /**
     * Get raw data view (something like serialization).
     * @return data value as String.
     */
    abstract fun getString(): String
}

/**
 * Representation of xlsx `B` value type.
 * @param value cell value.
 * @constructor Creates new instance with value.
 */
data class BooleanData(private var value: Boolean = false) : Data() {
    /**
     * Creates new instance from xlsx cell.
     * @param cell xlsx cell.
     */
    constructor(cell: Cell) : this(cell.v == "1")

    override fun asCell() = Cell().apply { t = STCellType.B; v = if (value) "1" else "0" }

    override fun asJsonValue() = value.toString()

    override fun asXmlValue() = value.toString()

    override fun getString() = value.toString()
}

/**
 * Representation of xlsx null value type.
 * @constructor Creates new instance.
 */
class NullData : Data() {
    override fun asCell() = Cell().apply { t = null; v = null }

    override fun asJsonValue() = "null"

    override fun asXmlValue() = "null"

    override fun getString() = "null"
}

/**
 * Representation of xlsx `N` value type.
 * @param number cell value.
 * @param numFormat number format.
 * @constructor Creates new instance with value.
 */
data class NumberData(private val number: Number?, private val numFormat: Long = 0) : Data() {
    private val value: Number? = if (number != null) {
        if (floor(number.toDouble()) == number.toDouble()) number.toInt() else number.toDouble()
    } else null
    private val serialized: String = if (value == null) "" else when (numFormat) {
        0L -> value.toString()
        14L -> SimpleDateFormat("dd.MM.yyyy").format(convertFromOADate(value.toDouble()))
        20L -> SimpleDateFormat("HH:mm").format(Calendar.getInstance().apply { time = convertFromOADate(value.toDouble()); add(Calendar.SECOND, 1) }.time)
        21L -> SimpleDateFormat("H:mm:ss").format(convertFromOADate(value.toDouble()))
        22L -> SimpleDateFormat("dd.MM.yyyy H:mm").format(convertFromOADate(value.toDouble()))
        else -> throw IllegalArgumentException("Number format $numFormat is not supported yet")
    }

    override fun asCell() = Cell().apply { t = STCellType.N; v = value.toString(); s = numFormat }

    override fun asJsonValue() = serialized

    override fun asXmlValue() = if (numFormat == 0L) serialized else "\"$serialized\""

    override fun getString() = serialized

    private fun convertFromOADate(d: Double): Date {
        val mantissa = d - d.toLong()
        val hour = mantissa * 24
        val min = (hour - hour.toLong()) * 60
        val sec = (min - min.toLong()) * 60
        return Calendar.getInstance().apply {
            time = SimpleDateFormat("dd MM yyyy").parse("30 12 1899")
            add(Calendar.DATE, d.toInt())
            add(Calendar.HOUR, hour.toInt())
            add(Calendar.MINUTE, min.toInt())
            add(Calendar.SECOND, sec.toInt())
        }.time
    }
}

/**
 * Representation of xlsx `S` and `INLINE_STR` value types.
 * @param value cell value.
 * @constructor Creates new instance with value.
 */
data class StringData(private val value: String) : Data() {
    /**
     * Creates new instance from xlsx cell.
     * @param spreadsheet xlsx spreadsheet which will be used to get shared string.
     * @param cell xlsx cell.
     */
    constructor(spreadsheet: SpreadsheetMLPackage, cell: Cell) : this((spreadsheet.parts[sharedStringsPart] as SharedStrings).jaxbElement.si[cell.v.toInt()].t.value)

    override fun asCell() = Cell().apply { t = STCellType.INLINE_STR; `is` = CTRst().apply { t = CTXstringWhitespace().apply { value = this@StringData.value } } }

    override fun asJsonValue() = value

    override fun asXmlValue() = "\"$value\""

    override fun getString() = value

    companion object {
        private val sharedStringsPart = PartName("/xl/sharedStrings.xml")
    }
}
