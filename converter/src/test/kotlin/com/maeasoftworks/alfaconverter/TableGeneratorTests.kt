package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.wrappers.Table
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class TableGeneratorTests {
	private val converter = Converter.ofFiles(
		Files.readAllBytes(Path.of("src/test/resources/table generator tests.xlsx")),
		Files.readAllBytes(Path.of("src/test/resources/to.xlsx")),
		"xlsx"
	).initialize()

	private val initial: Table
		get() = converter.documents.master.table

	@Test
	fun `size detection`() {
		assertEquals(10, initial.columns.size)
		assertEquals(1, initial.rowsCount)
		assertEquals(10, initial.headers.count())
	}

	@Test
	fun `string detection`() {
		assertEquals("Иванов", initial[0, 1]!!.value.getString())
	}

	@Test
	fun `number detection`() {
		assertEquals("59", initial[2, 1]!!.value.getString())
	}

	@Test
	fun `date detection`() {
		assertEquals("21.06.1963", initial[1, 1]!!.value.getString())
	}

	@Test
	fun `time detection with sec`() {
		assertEquals("9:20:00", initial[7, 1]!!.value.getString())
	}

	@Test
	fun `time detection without sec`() {
		assertEquals("13:53", initial[9, 1]!!.value.getString())
	}

	@Test
	fun `date & time detection`() {
		assertEquals("22.06.2022 9:20", initial[4, 1]!!.value.getString())
	}

	@Test
	fun `boolean detection`() {
		assertEquals("false", initial[3, 1]!!.value.getString())
	}
}