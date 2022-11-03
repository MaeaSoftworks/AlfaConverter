import com.maeasoftworks.alfaconverter.Conversion
import com.maeasoftworks.alfaconverter.Converter
import com.maeasoftworks.alfaconverter.actions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class Debug {
	@Test
	fun `launch test`() {
		Converter(
			Files.readAllBytes(Path.of("src/test/resources/from.xlsx")),
			Files.readAllBytes(Path.of("src/test/resources/to.xlsx")),
			Json.encodeToString(
				Conversion(
					arrayListOf(
						Bind(1, 2),
						Bind(2, 1),
						Split(4, listOf(5, 6, 7), "$5 $6 $7"),
						Merge(listOf(5, 6, 7), 4,"$5 - [$6, $7]")
					)
				)
			)
		).convert()
	}
}