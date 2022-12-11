package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.core.conversions.actions.Split
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.core.xml.Schema
import com.maeasoftworks.alfaconverter.core.xml.Xml
import com.maeasoftworks.alfaconverter.core.xml.structure.ComplexType
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import com.maeasoftworks.alfaconverter.core.xml.structure.Primitive
import com.maeasoftworks.alfaconverter.core.xml.structure.invoke
import com.maeasoftworks.alfaconverter.plugins.serializer
import kotlinx.serialization.encodeToString
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class XmlTests {
	private val schema = Schema(File("src/test/resources/xml/modifier.xsd").readText())

	private val converter = Converter(
		source = Xlsx(File("src/test/resources/xml/source.xlsx").readBytes()),
		result = Xml(Element("root").also { root ->
			root.type = ComplexType("root").also { rootType ->
				rootType.fields["person"] = ComplexType("person").also { person ->
					person.fields["firstname"] = Primitive.STRING()
					person.fields["lastname"] = Primitive.STRING()
					person.fields["middleName"] = Primitive.STRING()
					person.fields["birthday"] = Primitive.DATE()
					person.fields["age"] = Primitive.DECIMAL()
					person.fields["address"] = Primitive.STRING()
					person.fields["diagnosis"] = Primitive.STRING()
					person.fields["researchType"] = Primitive.STRING()
					person.fields["lab"] = ComplexType("lab").also { lab ->
						lab.attributes["address"] = Primitive.STRING()
						lab.attributes["name"] = Primitive.STRING()
						lab.attributes["code"] = Primitive.STRING()
					}
					person.fields["analysis"] = ComplexType("analysis").also { analysis ->
						analysis.attributes["dateStart"] = Primitive.DATE()
						analysis.attributes["timeStart"] = Primitive.TIME()
						analysis.attributes["dateComplete"] = Primitive.DATE()
						analysis.attributes["timeComplete"] = Primitive.TIME()
					}
				}
			}
		})
	)

	init {
		converter.initializeResultTable()
	}

	@Test
	fun `all types detected`() {
		assertEquals(schema.types.size, 4, "Expected 4 types")
	}

	@Test
	fun `conversion test`() {
		converter.conversion.actions += listOf(
			Split(
				"ФИО",
				listOf(
					"root.person.lastname",
					"root.person.firstname",
					"root.person.middleName"
				),
				"(\\S+) (\\S+) (\\S+)"
			),
			Bind("Дата рождения", "root.person.birthday"),
			Bind("Возраст пациента", "root.person.age"),
			Bind("Адрес прописки пациента", "root.person.address"),
			Merge(
				listOf("Диагноз (код)", "Диагноз (расшифровка)"),
				"root.person.diagnosis",
				"[\${Диагноз (код)}] \${Диагноз (расшифровка)}"
			),
			Bind("Тип исследования", "root.person.researchType"),
			Bind("Адрес лаборатории", "root.person.lab.address"),
			Bind("Название лаборатории", "root.person.lab.name"),
			Bind("Код лаборатории", "root.person.lab.code"),
			Bind("Дата взятия анализа", "root.person.analysis.dateStart"),
			Bind("Время взятия анализа", "root.person.analysis.timeStart"),
			Bind("Дата выполнения", "root.person.analysis.dateComplete"),
			Bind("Время выполнения анализа", "root.person.analysis.timeComplete")
		)
		println(serializer.encodeToString(converter.conversion))
		converter.executeActions()
		assertEquals("Иван", converter.result!!.table["root.person.firstname", 0]!!.getString())
		assertEquals("21.06.1963", converter.result!!.table["root.person.birthday", 0]!!.getString())
		assertEquals("13:53", converter.result!!.table["root.person.analysis.timeComplete", 0]!!.getString())
	}
}