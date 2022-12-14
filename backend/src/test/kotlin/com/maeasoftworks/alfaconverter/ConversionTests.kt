package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.conversions.actions.Cast
import com.maeasoftworks.alfaconverter.core.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.core.conversions.actions.Split
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.core.xlsx.structure.NumberData
import com.maeasoftworks.alfaconverter.core.xlsx.structure.StringData
import com.maeasoftworks.alfaconverter.core.xlsx.structure.TypeName
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class ConversionTests {
	private val converter = Converter(
		source = Xlsx(File("src/test/resources/conversion/source.xlsx").readBytes()),
		modifier = Xlsx(File("src/test/resources/conversion/modifier.xlsx").readBytes()),
		result = Xlsx()
	)

	init {
		converter.initializeResultTable()
	}

	private val result: Table
		get() = converter.result.table

	private val conversion: Conversion
		get() = converter.conversion

	@Test
	fun `binding test`() {
		conversion.actions += Bind(listOf("Column to bind 1"), listOf("bind 1 here"))
		conversion.actions += Cast(listOf("bind 1 here"), TypeName.Number, 0)
		converter.executeActions()
		for (row in result[listOf("bind 1 here")]!!.cells.indices) {
			val expected = NumberData((row + 1) * 10, 0)
			assertEquals(
				expected,
				result[listOf("bind 1 here")]!!.cells[row],
				"E: ${expected.getString()}; A: ${result[listOf("bind 1 here")]!!.cells[row].getString()}"
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
		conversion.actions += Split(
			listOf("Will be split"),
			listOf(listOf("was"), listOf("split"), listOf("!!!")),
			"(\\S+) (\\S+) (\\S+)"
		)
		converter.executeActions()
		var pos = 0
		for (columnPos in 2..4) {
			val column = result.columns[columnPos].cells
			for (cell in column.indices) {
				val expected = StringData(getString(pos++ + columnPos - 2))
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

		conversion.actions += Merge(
			listOf(listOf("Will"), listOf("be"), listOf("merged")),
			listOf("merged"),
			"\${0} \${1} \${2}"
		)
		converter.executeActions()
		var pos = 0
		for (cell in result[listOf("merged")]!!.cells) {
			assertEquals(
				StringData(getString(pos++)),
				cell
			)
		}
	}
}