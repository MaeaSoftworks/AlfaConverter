package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.core.xml.Schema
import com.maeasoftworks.alfaconverter.core.xml.Xml
import com.maeasoftworks.alfaconverter.core.xml.structure.ComplexType
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import com.maeasoftworks.alfaconverter.core.xml.structure.Primitive
import com.maeasoftworks.alfaconverter.plugins.serializer
import kotlinx.serialization.encodeToString
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class XmlTests {
	private val schema = Schema(File("src/test/resources/example.xsd").readText())

	private val converter = Converter(
		source = Xlsx(File("src/test/resources/from.xlsx").readBytes()),
		result = Xml(Element("person").also {
			it.type = ComplexType("person").build {
				"name" of Primitive.STRING
				"birthday" of Primitive.DATE
				"age" of Primitive.DECIMAL
				"address" of Primitive.STRING
				"diagnosis" of ComplexType("diagnosis").build {
					"code" of Primitive.STRING
					"name" of Primitive.STRING
				}
				"researchType" of Primitive.STRING
				"lab" of ComplexType("lab").build {
					"address" of Primitive.STRING
					"name" of Primitive.STRING
					"code" of Primitive.STRING
				}
				"analysis" of ComplexType("analysis").build {
					"dateStart" of Primitive.DATE
					"timeStart" of Primitive.TIME
					"dateComplete" of Primitive.DATE
					"timeComplete" of Primitive.TIME
				}
			}
		})
	)

	init {
		converter.initializeResultTable()
	}

	@Test
	fun `all types detected`() {
		assertEquals(schema.types.size, 5, "Expected 5 types")
	}

	@Test
	fun `binding test`() {
		converter.conversion.actions += listOf(
			Bind("ФИО", "person.name"),
			Bind("Дата рождения", "person.birthday"),
			Bind("Возраст пациента", "person.age"),
			Bind("Адрес прописки пациента", "person.address"),
			Bind("Диагноз (код)", "person.diagnosis.code"),
			Bind("Диагноз (расшифровка)", "person.diagnosis.name"),
			Bind("Тип исследования", "person.person.researchType"),
			Bind("Адрес лаборатории", "person.lab.address"),
			Bind("Название лаборатории", "person.lab.name"),
			Bind("Код лаборатории", "person.lab.code"),
			Bind("Дата взятия анализа", "person.analysis.dateStart"),
			Bind("Время взятия анализа", "person.analysis.timeStart"),
			Bind("Дата выполнения", "person.analysis.dateComplete"),
			Bind("Время выполнения анализа", "person.analysis.timeComplete")
		)
		println(serializer.encodeToString(converter.conversion))
		converter.executeActions()
		assertEquals("Иванов Иван Иванович", converter.result!!.table["person.name", 0]!!.getString())
		assertEquals("21.06.1963", converter.result!!.table["person.birthday", 0]!!.getString())
		assertEquals("13:53", converter.result!!.table["person.analysis.timeComplete", 0]!!.getString())
	}
}