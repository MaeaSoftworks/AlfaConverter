package com.maeasoftworks.alfaconverter.wrappers

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings
import org.docx4j.openpackaging.parts.SpreadsheetML.Styles
import org.xlsx4j.sml.STCellType
import java.io.InvalidClassException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


internal class Cell(
	var row: Int,
	var column: Int
) {
	var value: Any? = null
	var stringValue: String? = null
	lateinit var wrapped: org.xlsx4j.sml.Cell
	var format: Long = -1

	override fun equals(other: Any?): Boolean {
		return other != null
				&& other is Cell
				&& row == other.row
				&& column == other.column
				&& value == other.value
				&& stringValue == other.stringValue
	}

	override fun hashCode(): Int {
		var result = value?.hashCode() ?: 0
		result = 31 * result + wrapped.hashCode()
		return result
	}

	companion object {
		private val sharedStringsPart = PartName("/xl/sharedStrings.xml")
		private val stylesPart = PartName("/xl/styles.xml")

		fun extractValue(
			docx4jCell: org.xlsx4j.sml.Cell,
			spreadsheet: SpreadsheetMLPackage,
			row: Int,
			column: Int
		): Cell {
			val cell = Cell(row, column)
			cell.wrapped = docx4jCell
			when (docx4jCell.t) {
				STCellType.B -> {
					cell.value = docx4jCell.f.value == "TRUE"
					cell.stringValue = cell.value.toString().uppercase(Locale.getDefault())
				}

				STCellType.N -> {
					parseNumber(cell, docx4jCell, spreadsheet)
				}

				STCellType.E -> {
					cell.value = "error"
					cell.stringValue = "error"
				}

				STCellType.S -> {
					cell.value = findString(spreadsheet, docx4jCell.v)
					cell.stringValue = cell.value as String
				}

				STCellType.STR -> {
					cell.value = "str"
					cell.stringValue = "str"
				}

				STCellType.INLINE_STR -> {
					cell.value = "inline"
					cell.stringValue = "inline"
				}

				null -> {
					cell.value = "null"
					cell.stringValue = "null"
				}
			}
			return cell
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

		private fun convertFromOADate(d: Double): Date? {
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

		private fun parseNumber(cell: Cell, docx4jCell: org.xlsx4j.sml.Cell, spreadsheet: SpreadsheetMLPackage) {
			val style = (spreadsheet.parts[stylesPart] as Styles).getXfByIndex(docx4jCell.s)
			when (style.numFmtId) {
				0L -> {
					cell.value = docx4jCell.v.toInt()
					cell.stringValue = cell.value.toString()
				}

				1L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				2L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				3L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				4L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				9L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				10L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				11L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				12L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				13L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				14L -> {
					val date = convertFromOADate(docx4jCell.v.toDouble())
					cell.value = date
					cell.stringValue = SimpleDateFormat("dd.MM.yyyy").format(date)
				}

				15L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				16L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				17L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				18L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				19L -> {
					cell.stringValue = style.numFmtId.toString()
				} //?????
				20L -> {
					val date = convertFromOADate(docx4jCell.v.toDouble())
					cell.value = date
					val c = Calendar.getInstance().also {
						it.time = date
						it.add(Calendar.SECOND, 1)
					}.time
					cell.stringValue = SimpleDateFormat("HH:mm").format(c)
				}

				21L -> {
					val date = convertFromOADate(docx4jCell.v.toDouble())
					cell.value = date
					cell.stringValue = SimpleDateFormat("H:mm:ss").format(date)
				}

				22L -> {
					val date = convertFromOADate(docx4jCell.v.toDouble())
					cell.value = date
					cell.stringValue = SimpleDateFormat("dd.MM.yyyy H:mm").format(date)
				}

				else -> throw InvalidClassException("Unknown number format")
			}
		}

		private fun findString(spreadsheet: SpreadsheetMLPackage, link: String): String {
			val strings = spreadsheet.parts[sharedStringsPart] as SharedStrings
			return strings.jaxbElement.si[link.toInt()].t.value
		}
	}
}