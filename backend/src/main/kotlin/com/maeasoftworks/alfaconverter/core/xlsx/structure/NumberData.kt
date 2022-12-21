package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

data class NumberData(private val number: Number, private val numFormat: Long = 0) : Data() {
    private val value: Number = if (floor(number.toDouble()) == number.toDouble()) number.toInt() else number.toDouble()
    private val serialized: String = when (numFormat) {
        0L -> value.toString()
        14L -> SimpleDateFormat("dd.MM.yyyy").format(convertFromOADate(value.toDouble()))
        20L -> SimpleDateFormat("HH:mm").format(Calendar.getInstance().apply { time = convertFromOADate(value.toDouble()); add(Calendar.SECOND, 1) }.time)
        21L -> SimpleDateFormat("H:mm:ss").format(convertFromOADate(value.toDouble()))
        22L -> SimpleDateFormat("dd.MM.yyyy H:mm").format(convertFromOADate(value.toDouble()))
        else -> throw IllegalArgumentException("Number format $numFormat is not supported yet")
    }

    override fun getXlsxRepresentation() = Cell().apply { t = STCellType.N; v = value.toString(); s = numFormat }

    override fun getJsonRepresentation() = serialized

    override fun getXmlRepresentation() = if (numFormat == 0L) serialized else "\"$serialized\""

    override fun getString() = serialized

    private fun convertFromOADate(d: Double): Date {
        val mantissa = d - d.toLong()
        val hour = mantissa * 24
        val min = (hour - hour.toLong()) * 60
        val sec = (min - min.toLong()) * 60
        val myFormat = SimpleDateFormat("dd MM yyyy")
        val baseDate: Date = myFormat.parse("30 12 1899")
        val c = Calendar.getInstance()
        c.time = baseDate
        c.add(Calendar.DATE, d.toInt())
        c.add(Calendar.HOUR, hour.toInt())
        c.add(Calendar.MINUTE, min.toInt())
        c.add(Calendar.SECOND, sec.toInt())
        return c.time
    }
}
