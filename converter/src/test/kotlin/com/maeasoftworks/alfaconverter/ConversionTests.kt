package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.conversions.Conversion
import com.maeasoftworks.alfaconverter.conversions.TypeConversion
import com.maeasoftworks.alfaconverter.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.conversions.actions.Split
import com.maeasoftworks.alfaconverter.model.datatypes.XNumber
import com.maeasoftworks.alfaconverter.model.datatypes.XString
import com.maeasoftworks.alfaconverter.model.datatypes.XTypeName
import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ConversionTests {
	private val converter = Converter.ofTables(ExampleTables.tableFrom, ExampleTables.tableTo).initialize()

	private val result: Table
		get() = converter.documents.slave.table

	private val conversion: Conversion
		get() = converter.conversion

	@Test
	fun `binding test`() {
		conversion.addAction(Bind(0, 1))
		conversion.addTypeConversion(1, TypeConversion(XTypeName.XNumber, 0))
		conversion.start()
		for (row in 1..result.columns[1]!!.cells.values.size) {
			val expected = Cell(row, 1).also { it.value = XNumber((row) * 10, 0) }
			assertEquals(
				expected,
				result.columns[1]!!.cells[row]
			)
		}
	}

	@Test
	fun `split test`() {
		val getString = { x: Int ->
			when (x % 3) {
				0 -> "c"; 1 -> "a"; 2 -> "b"; else -> "d"
			}
		}
		conversion.addAction(Split(2, listOf(2, 3, 4), "(\\S+) (\\S+) (\\S+)"))
		conversion.start()
		for (column in 2..4) {
			for (row in result.columns[column]!!.cells.keys) {
				assertEquals(
					Cell(row, column).also {
						it.value = XString(getString(row + column - 2))
					},
					result.columns[column]!!.cells[row]
				)
			}
		}
	}

	@Test
	fun `merge test`() {
		val getString = { x: Int ->
			when (x % 2) {
				0 -> "3 2 1"; 1 -> "1 2 3"; else -> "0 0 0"
			}
		}

		conversion.addAction(Merge(listOf(3, 4, 5), 5, "$3 $4 $5"))
		conversion.start()
		for (row in result.columns[5]!!.cells.keys) {
			assertEquals(
				Cell(row, 5).also { it.value = XString(getString(row)) },
				result.columns[5]!!.cells[row]
			)
		}
	}
}