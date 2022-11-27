package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.model.Schema
import org.junit.jupiter.api.Test
import java.io.File

class XsdParsingTests {
	@Test
	fun parsing() {
		val model = Schema(File("src/test/resources/example.xsd").readText())
		println()
	}
}