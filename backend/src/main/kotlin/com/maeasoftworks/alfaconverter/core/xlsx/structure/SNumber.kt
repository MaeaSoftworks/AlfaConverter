package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType
import java.io.InvalidClassException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class SNumber(value: Number, dataFormat: Long = 0) : SObject() {
	private var value: Any
	private var rawValue: Number = 0
	private var numFormat: Long? = dataFormat
	private var string: String

	init {
		rawValue = if (floor(value.toDouble()) == value.toDouble()) value.toInt() else value.toDouble()
		this.value = rawValue
		when (this.numFormat) {
			0L -> {
				string = this.value.toString()
			}

			14L -> {
				val date = convertFromOADate(rawValue.toDouble())
				this.value = date
				string = SimpleDateFormat("dd.MM.yyyy").format(date)
			}

			20L -> {
				val date = convertFromOADate(rawValue.toDouble())
				this.value = date
				val c = Calendar.getInstance().also {
					it.time = date
					it.add(Calendar.SECOND, 1)
				}.time
				string = SimpleDateFormat("HH:mm").format(c)
			}

			21L -> {
				val date = convertFromOADate(rawValue.toDouble())
				this.value = date
				string = SimpleDateFormat("H:mm:ss").format(date)
			}

			22L -> {
				val date = convertFromOADate(rawValue.toDouble())
				this.value = date
				string = SimpleDateFormat("dd.MM.yyyy H:mm").format(date)
			}

			else -> throw InvalidClassException("Unknown number format")
		}
	}

	override fun getXlsxRepresentation(): Cell {
		return Cell().also {
			it.t = STCellType.N
			it.v = rawValue.toString()
			it.s = numFormat!!
		}
	}

	override fun getJsonRepresentation(): String {
		return string
	}

	override fun getXmlRepresentation(): String {
		return if (numFormat == 0L) string else "\"$string\""
	}

	override fun getString(): String {
		return string
	}

	override fun equals(other: Any?): Boolean {
		return other != null
				&& other is SNumber
				&& rawValue == other.rawValue
				&& numFormat == other.numFormat
	}

	override fun hashCode(): Int {
		var result = rawValue.hashCode()
		result = 31 * result + numFormat.hashCode()
		return result
	}

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