package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class TableGeneratorTests {
    private val converter = Converter(
        Xlsx(Files.readAllBytes(Path.of("src/test/resources/table generator/source.xlsx")))
    )

    private val initial: Table
        get() = converter.source.table

    @Test
    fun `size detection`() {
        assertEquals(10, initial.values.size)
        assertEquals(1, initial.rowsCount)
        assertEquals(10, initial.headers.count())
    }

    @Test
    fun `string detection`() {
        assertEquals("Иванов", initial[listOf("Фамилия"), 0]!!.getString())
    }

    @Test
    fun `number detection`() {
        assertEquals("59", initial[listOf("Возраст"), 0]!!.getString())
    }

    @Test
    fun `date detection`() {
        assertEquals("21.06.1963", initial[listOf("Дата рождения"), 0]!!.getString())
    }

    @Test
    fun `time detection with sec`() {
        assertEquals("9:20:00", initial[listOf("Время взятия анализа"), 0]!!.getString())
    }

    @Test
    fun `time detection without sec`() {
        assertEquals("13:53", initial[listOf("Время выполнения анализа"), 0]!!.getString())
    }

    @Test
    fun `date & time detection`() {
        assertEquals("22.06.2022 9:20", initial[listOf("Дата и время взятия анализа"), 0]!!.getString())
    }

    @Test
    fun `boolean detection`() {
        assertEquals("false", initial[listOf("Жив"), 0]!!.getString())
    }
}
