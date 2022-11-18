package com.maeasoftworks.alfaconverter.model.datatypes

import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType
import java.io.InvalidClassException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor

class XNumber(value: Number, dataFormat: Long = 0) : XObject() {
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

			1L -> {
				string = numFormat.toString()
			} //?????
			2L -> {
				string = numFormat.toString()
			} //?????
			3L -> {
				string = numFormat.toString()
			} //?????
			4L -> {
				string = numFormat.toString()
			} //?????
			9L -> {
				string = numFormat.toString()
			} //?????
			10L -> {
				string = numFormat.toString()
			} //?????
			11L -> {
				string = numFormat.toString()
			} //?????
			12L -> {
				string = numFormat.toString()
			} //?????
			13L -> {
				string = numFormat.toString()
			} //?????
			14L -> {
				val date = convertFromOADate(rawValue.toDouble())
				this.value = date
				string = SimpleDateFormat("dd.MM.yyyy").format(date)
			}

			15L -> {
				string = numFormat.toString()
			} //?????
			16L -> {
				string = numFormat.toString()
			} //?????
			17L -> {
				string = numFormat.toString()
			} //?????
			18L -> {
				string = numFormat.toString()
			} //?????
			19L -> {
				string = numFormat.toString()
			} //?????
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

	override fun getXmlRepresentation(): Any? {
		return string
	}

	override fun getString(): String {
		return string
	}

	override fun equals(other: Any?): Boolean {
		return other != null
				&& other is XNumber
				&& rawValue == other.rawValue
				&& numFormat == other.numFormat
	}

	override fun hashCode(): Int {
		var result = rawValue.hashCode()
		result = 31 * result + numFormat.hashCode()
		return result
	}

	fun convertToOADate(date: Date): String {
		val oaDate: Double
		val myFormat = SimpleDateFormat("dd.MM.yyyy")
		val baseDate = myFormat.parse("30.12.1899")
		val days = TimeUnit.DAYS.convert(date.time - baseDate.time, TimeUnit.MILLISECONDS)
		oaDate = days.toDouble() + date.hours.toDouble() / 24 + date.minutes.toDouble() / (60 * 24) + date.seconds
			.toDouble() / (60 * 24 * 60)
		return oaDate.toString()
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