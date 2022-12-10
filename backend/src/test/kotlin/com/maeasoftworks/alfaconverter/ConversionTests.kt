package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.xlsx.XlsxConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.conversions.actions.Cast
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.core.conversions.actions.Split
import com.maeasoftworks.alfaconverter.core.xlsx.structure.SNumber
import com.maeasoftworks.alfaconverter.core.xlsx.structure.SString
import com.maeasoftworks.alfaconverter.core.xlsx.structure.STypeName
import com.maeasoftworks.alfaconverter.core.model.Table
import org.junit.Test
import kotlin.test.assertEquals

class ConversionTests {
	private val xlsxConverter = XlsxConverter(ExampleTables.tableFrom, ExampleTables.tableTo)

	private val result: Table
		get() = xlsxConverter.targetSpreadsheet.table

	private val conversion: Conversion
		get() = xlsxConverter.conversion

	@Test
	fun `binding test`() {
		conversion.actions += Bind("Column to bind 1", "Column to bind 2")
		conversion.actions += Cast("Column to bind 2", STypeName.SNumber, 0)
		xlsxConverter.executeActions()
		for (row in result["Column to bind 2"]!!.cells.indices) {
			val expected = SNumber((row + 1) * 10, 0)
			assertEquals(
				expected,
				result["Column to bind 2"]!!.cells[row],
				"E: ${expected.getString()}; A: ${result["Column to bind 2"]!!.cells[row].getString()}"
			)
		}
	}

	@Test
	fun `split test`() {
		val getString = { x: Int ->
			when (x % 3) {
				0 -> "a"; 1 -> "b"; 2 -> "c"; else -> "d"
			}
		}
		conversion.actions += Split("Will be split", listOf("Will", "be", "split"), "(\\S+) (\\S+) (\\S+)")
		xlsxConverter.executeActions()
		var pos = 0
		for (columnPos in 3..5) {
			val column = result.columns[columnPos].cells
			for (cell in column.indices) {
				val expected = SString(getString(pos++ + columnPos - 3))
				assertEquals(
					expected,
					column[cell],
					"E: ${expected.getString()}; A: ${column[cell].getString()}; at ${columnPos}:${cell}"
				)
			}
		}
	}

	@Test
	fun `merge test`() {
		val getString = { x: Int ->
			when (x % 2) {
				0 -> "1 2 3"; 1 -> "3 2 1"; else -> "0 0 0"
			}
		}

		conversion.actions += Merge(listOf("Will", "be", "merged"), "Will be merged", "\${Will} \${be} \${merged}")
		xlsxConverter.executeActions()
		var pos = 0
		for (cell in result["Will be merged"]!!.cells) {
			assertEquals(
				SString(getString(pos++)),
				cell
			)
		}
	}
}