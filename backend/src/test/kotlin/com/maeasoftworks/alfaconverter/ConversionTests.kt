package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.XlsxConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.conversions.TypeConversion
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.core.conversions.actions.Split
import com.maeasoftworks.alfaconverter.core.conversions.pos
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SNumber
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.STypeName
import com.maeasoftworks.alfaconverter.core.model.Table
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ConversionTests {
	private val xlsxConverter = XlsxConverter(ExampleTables.tableFrom, ExampleTables.tableTo)

	private val result: Table
		get() = xlsxConverter.target.table

	private val conversion: Conversion
		get() = xlsxConverter.conversion

	@Test
	fun `binding test`() {
		conversion.addAction(Bind(0.pos, 1.pos))
		conversion.addTypeConversion(1.pos, TypeConversion(STypeName.SNumber, 0))
		conversion.start()
		for (row in 1..result.columns[1.pos]!!.cells.values.size) {
			val expected = Table.Cell(1.pos, row).also { it.value = SNumber((row) * 10, 0) }
			assertEquals(
				expected,
				result.columns[1.pos]!!.cells[row]
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
		conversion.addAction(Split(2.pos, listOf(2.pos, 3.pos, 4.pos), "(\\S+) (\\S+) (\\S+)"))
		conversion.start()
		for (column in 2..4) {
			for (row in result.columns[column.pos]!!.cells.keys) {
				assertEquals(
					Table.Cell(column.pos, row).also {
						it.value = SString(getString(row + column - 2))
					},
					result.columns[column.pos]!!.cells[row]
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

		conversion.addAction(Merge(listOf(3.pos, 4.pos, 5.pos), 5.pos, "$3 $4 $5"))
		conversion.start()
		for (row in result.columns[5.pos]!!.cells.keys) {
			assertEquals(
				Table.Cell(5.pos, row).also { it.value = SString(getString(row)) },
				result.columns[5.pos]!!.cells[row]
			)
		}
	}
}