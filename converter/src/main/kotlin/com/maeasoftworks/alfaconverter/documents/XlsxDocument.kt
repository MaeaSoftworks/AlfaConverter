package com.maeasoftworks.alfaconverter.documents

import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings
import org.docx4j.openpackaging.parts.SpreadsheetML.Styles
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import org.xlsx4j.jaxb.Context
import org.xlsx4j.sml.CTRst
import org.xlsx4j.sml.ObjectFactory
import org.xlsx4j.sml.STCellType
import org.xlsx4j.sml.Worksheet
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InvalidClassException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.NoSuchElementException

internal class XlsxDocument : Document() {
	private lateinit var document: SpreadsheetMLPackage
	private lateinit var worksheet: Worksheet

	override fun open(file: ByteArray) : Document {
		document = SpreadsheetMLPackage.load(ByteArrayInputStream(file))
		worksheet = (document.parts[sheet] as WorksheetPart).jaxbElement
		return this
	}

	override fun createTable(): Table {
		val table = Table()
		for (row in worksheet.sheetData.row.indices) {
			for (cell in worksheet.sheetData.row[row].c.indices) {
				table.append(cell, row, extractValue(worksheet.sheetData.row[row].c[cell], row, cell))
			}
		}
		extractHeaders(table)
		return table
	}

	override fun getHeaders(): List<String?> {
		val headers: MutableList<Cell> = mutableListOf()
		if (worksheet.sheetData.row[0].c.isEmpty()) throw NoSuchElementException("First row of table was empty")
		for (column in worksheet.sheetData.row[0].c.indices) {
			headers.add(extractValue(worksheet.sheetData.row[0].c[column], 0, column))
		}
		return headers.map { it.stringValue }
	}

	override fun save(): ByteArray {
		val pkg = SpreadsheetMLPackage.createPackage()
		val factory = Context.getsmlObjectFactory()

		val sheet = pkg.createWorksheetPart(PartName("/xl/worksheets/sheet1.xml"), "result", 1)
		val sheetData = sheet.contents.sheetData

		val header = factory.createRow()
		for (columnNumber in 0 until table.columns.size) {
			val cell = createCell(table.headers[columnNumber].stringValue, factory)
			cell.r = toExcel(columnNumber) + "1"
			header.c.add(cell)
		}
		sheetData.row.add(header)

		for (rowNumber in 1 until table.rowsCount) {
			val row = factory.createRow()
			for (columnNumber in 0 until table.columns.size) {
				val cell = createCell(table.columns[columnNumber]?.get(rowNumber)?.stringValue, factory)
				cell.r = toExcel(columnNumber) + (rowNumber + 1).toString()
				row.c.add(cell)
			}
			sheetData.row.add(row)
		}
		val stream = ByteArrayOutputStream()
		pkg.save(stream)
		return stream.toByteArray()
	}

	private fun extractHeaders(table: Table): Table {
		table.columns.forEach { (key, value) ->
			table.headers.add(value.cells.values.first())
			table.columns[key]!!.cells.remove(0)
		}
		return table
	}

	private fun createCell(content: String?, factory: ObjectFactory): org.xlsx4j.sml.Cell {
		val cell = factory.createCell()
		val ctx = factory.createCTXstringWhitespace()
		ctx.value = content
		val ctr = CTRst()
		ctr.t = ctx
		cell.t = STCellType.INLINE_STR
		cell.`is` = ctr
		return cell
	}

	private fun extractValue(docx4jCell: org.xlsx4j.sml.Cell, row: Int, column: Int): Cell {
		val cell = Cell(row, column)
		cell.wrapped = docx4jCell
		when (docx4jCell.t) {
			STCellType.B -> {
				cell.value = docx4jCell.f.value == "TRUE"
				cell.stringValue = cell.value.toString().uppercase(Locale.getDefault())
			}

			STCellType.N -> {
				parseNumber(cell, docx4jCell, document)
			}

			STCellType.E -> {
				cell.value = "error"
				cell.stringValue = "error"
			}

			STCellType.S -> {
				cell.value = findString(document, docx4jCell.v)
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

	companion object {
		private val sheet = PartName("/xl/worksheets/sheet1.xml")
		private val alphabet = ('A'..'Z').toMutableList()
		private val sharedStringsPart = PartName("/xl/sharedStrings.xml")
		private val stylesPart = PartName("/xl/styles.xml")

		fun toExcel(number: Int): String {
			var num = number
			fun divAndMod(n: Int): Pair<Int, Int> {
				val a = n / 26
				val b = n % 26
				return if (b == 0) Pair(a - 1, 26) else Pair(a, b)
			}

			val chars = mutableListOf<Char>()
			while (num > 0) {
				val param = divAndMod(num)
				num = param.first
				chars.add(alphabet[param.second])
			}
			return chars.joinToString { it.toString() }
		}
	}
}