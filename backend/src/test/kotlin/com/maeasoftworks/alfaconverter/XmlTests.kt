package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.core.conversions.Path
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.ComplexType
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XPrimitive
import com.maeasoftworks.alfaconverter.core.model.Schema
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
			Bind(Path( 0), Path("person.name")),
			Bind(Path( 1), Path("person.birthday")),
			Bind(Path( 2), Path("person.age")),
			Bind(Path( 3), Path("person.address")),
			Bind(Path( 4), Path("person.diagnosis.code")),
			Bind(Path( 5), Path("person.diagnosis.name")),
			Bind(Path( 6), Path("person.person.researchType")),
			Bind(Path( 7), Path("person.lab.address")),
			Bind(Path( 8), Path("person.lab.name")),
			Bind(Path( 9), Path("person.lab.code")),
			Bind(Path(10), Path("person.analysis.dateStart")),
			Bind(Path(11), Path("person.analysis.timeStart")),
			Bind(Path(12), Path("person.analysis.dateComplete")),
			Bind(Path(13), Path("person.analysis.timeComplete"))
		)
		converter.conversion.start()
		assertEquals("Иванов Иван Иванович", converter.schema.table[Path("person.name"), 1]!!.value.getString())
		assertEquals("21.06.1963", converter.schema.table[Path("person.birthday"), 1]!!.value.getString())
		assertEquals("13:53", converter.schema.table[Path("person.analysis.timeComplete"), 1]!!.value.getString())
	}
}