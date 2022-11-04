import com.maeasoftworks.alfaconverter.Conversion
import com.maeasoftworks.alfaconverter.Converter
import com.maeasoftworks.alfaconverter.actions.Bind
import com.maeasoftworks.alfaconverter.actions.Merge
import com.maeasoftworks.alfaconverter.actions.Split
import com.maeasoftworks.alfaconverter.wrappers.Cell
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConversionTests {
	private val converter = Converter(
		Files.readAllBytes(Path.of("src/test/resources/conversions_from.xlsx")),
		Files.readAllBytes(Path.of("src/test/resources/conversions_to.xlsx")),
		Json.encodeToString(Conversion(arrayListOf()))
	).initialize()

	@Test
	fun `binding test`() {
		converter.conversion.addAction(Bind(0, 1))
		converter.conversion.start()
		for (cell in converter.resultTable.columns[0]!!.cells) {
			assertNull(cell.value)
		}
		for (row in converter.resultTable.columns[1]!!.cells.values.indices) {
			assertEquals(
				Cell(row + 1, 1).also { it.value = (row + 1) * 10; it.stringValue = ((row + 1) * 10).toString() },
				converter.resultTable.columns[1]!!.cells[row + 1]
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

		converter.conversion.addAction(Split(2, listOf(2, 3, 4), "(\\S+) (\\S+) (\\S+)"))
		converter.conversion.start()
		for (column in 2..4) {
			for (row in converter.resultTable.columns[column]!!.cells.keys) {
				assertEquals(
					Cell(row, column).also {
						it.value = getString(row + column - 2); it.stringValue = getString(row + column - 2)
					},
					converter.resultTable.columns[column]!!.cells[row]
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

		converter.conversion.addAction(Merge(listOf(3, 4, 5), 5, "$3 $4 $5"))
		converter.conversion.start()
		for (row in converter.resultTable.columns[5]!!.cells.keys) {
			assertEquals(
				Cell(row, 5).also { it.value = getString(row); it.stringValue = getString(row) },
				converter.resultTable.columns[5]!!.cells[row]
			)
		}
	}
}