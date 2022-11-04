import com.maeasoftworks.alfaconverter.Conversion
import com.maeasoftworks.alfaconverter.Converter
import com.maeasoftworks.alfaconverter.actions.Bind
import com.maeasoftworks.alfaconverter.actions.Split
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ConversionTests {
	val converter = Converter(
		Files.readAllBytes(Path.of("src/test/resources/from.xlsx")),
		Files.readAllBytes(Path.of("src/test/resources/to.xlsx")),
		Json.encodeToString(
			Conversion(
				arrayListOf(
					Split(0, listOf(1, 2, 3), "(\\w+) (\\w+) (\\w+)"),
					Bind(2, 2),
					Bind(3, 5),
					//Merge(listOf(5, 6, 7), 4,"$5 - [$6, $7]")
				)
			)
		)
	).initialize()
}