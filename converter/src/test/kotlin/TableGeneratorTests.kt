import com.maeasoftworks.alfaconverter.Conversion
import com.maeasoftworks.alfaconverter.Converter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class TableGeneratorTests {
	private val converter = Converter(
		Files.readAllBytes(Path.of("src/test/resources/table generator tests.xlsx")),
		Files.readAllBytes(Path.of("src/test/resources/to.xlsx")),
		Json.encodeToString(Conversion(arrayListOf()))
	).initialize()

	@Test
	fun `size detection`() {
		assertEquals(10, converter.initialTable.columns.size)
		assertEquals(1, converter.initialTable.rowsCount)
		assertEquals(10, converter.initialTable.headers.count())
	}

	@Test
	fun `string detection`() {
		assertEquals("Иванов", converter.initialTable[0, 1]!!.value)
	}

	@Test
	fun `number detection`() {
		assertEquals(59, converter.initialTable[2, 1]!!.value)
	}

	@Test
	fun `date detection`() {
		assertEquals("21.06.1963", converter.initialTable[1, 1]!!.stringValue)
	}

	@Test
	fun `time detection with sec`() {
		assertEquals("9:20:00", converter.initialTable[7, 1]!!.stringValue)
	}

	@Test
	fun `time detection without sec`() {
		assertEquals("13:53", converter.initialTable[9, 1]!!.stringValue)
	}

	@Test
	fun `date & time detection`() {
		assertEquals("22.06.2022 9:20", converter.initialTable[4, 1]!!.stringValue)
	}

	@Test
	fun `boolean detection`() {
		assertEquals("FALSE", converter.initialTable[3, 1]!!.stringValue)
	}
}