package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.conversions.Conversion
import com.maeasoftworks.alfaconverter.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.conversions.actions.Split
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

class SavingManualTest {
	private val converter = Converter.ofFiles(
		Files.readAllBytes(Path.of("src/test/resources/conversions_from.xlsx")),
		Files.readAllBytes(Path.of("src/test/resources/conversions_to.xlsx")),
		"xlsx"
	)
		.setConversion(
			Conversion(
				mutableListOf(
					Bind(0, 1),
					Bind(1, 0),
					Split(2, listOf(2, 3, 4), "(\\S+) (\\S+) (\\S+)"),
					Merge(listOf(3, 4, 5), 5, "$3 $4 $5")
				)
			)
		)
		.initialize()

	@Test
	fun `saving test`() {
		Files.write(Path.of("src/test/resources/conversions_result.xlsx"), converter.convert())
	}
}