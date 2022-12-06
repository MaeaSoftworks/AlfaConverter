package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.ComplexType
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XPrimitive
import com.maeasoftworks.alfaconverter.core.model.Schema
import com.maeasoftworks.alfaconverter.plugins.serializer
import kotlinx.serialization.encodeToString
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class XmlTests {
	private val schema = Schema(File("src/test/resources/example.xsd").readText())

	private val converter = XmlConverter(
		File("src/test/resources/from.xlsx").readBytes(),
		listOf(
			Element("person").also {
				it.type = ComplexType("person").build {
					"name" of XPrimitive.STRING
					"birthday" of XPrimitive.DATE
					"age" of XPrimitive.DECIMAL
					"address" of XPrimitive.STRING
					"diagnosis" of ComplexType("diagnosis").build {
						"code" of XPrimitive.STRING
						"name" of XPrimitive.STRING
					}
					"researchType" of XPrimitive.STRING
					"lab" of ComplexType("lab").build {
						"address" of XPrimitive.STRING
						"name" of XPrimitive.STRING
						"code" of XPrimitive.STRING
					}
					"analysis" of ComplexType("analysis").build {
						"dateStart" of XPrimitive.DATE
						"timeStart" of XPrimitive.TIME
						"dateComplete" of XPrimitive.DATE
						"timeComplete" of XPrimitive.TIME
					}
				}
			}
		)
	)

	@Test
	fun `all types detected`() {
		assertEquals(schema.types.size, 5, "Expected 5 types")
	}

	@Test
	fun `binding test`() {
		converter.conversion.addActions(
			Bind("ФИО",  "person.name"),
			Bind("Дата рождения",  "person.birthday"),
			Bind("Возраст пациента",  "person.age"),
			Bind("Адрес прописки пациента",  "person.address"),
			Bind("Диагноз (код)",  "person.diagnosis.code"),
			Bind("Диагноз (расшифровка)",  "person.diagnosis.name"),
			Bind("Тип исследования",  "person.person.researchType"),
			Bind("Адрес лаборатории",  "person.lab.address"),
			Bind("Название лаборатории",  "person.lab.name"),
			Bind("Код лаборатории",  "person.lab.code"),
			Bind("Дата взятия анализа", "person.analysis.dateStart"),
			Bind("Время взятия анализа", "person.analysis.timeStart"),
			Bind("Дата выполнения", "person.analysis.dateComplete"),
			Bind("Время выполнения анализа", "person.analysis.timeComplete")
		)
		println(serializer.encodeToString(converter.conversion))
		converter.conversion.start()
		assertEquals("Иванов Иван Иванович", converter.schema.table["person.name", 0]!!.getString())
		assertEquals("21.06.1963", converter.schema.table["person.birthday", 0]!!.getString())
		assertEquals("13:53", converter.schema.table["person.analysis.timeComplete", 0]!!.getString())
	}
}