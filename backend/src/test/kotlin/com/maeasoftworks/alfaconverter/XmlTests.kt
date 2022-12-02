package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.conversions.at
import com.maeasoftworks.alfaconverter.core.conversions.pos
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.ComplexType
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XPrimitive
import com.maeasoftworks.alfaconverter.core.model.Schema
import org.junit.jupiter.api.Test
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
			Bind(0.pos, "name" at "person"),
			Bind(1.pos, "birthday" at "person"),
			Bind(2.pos, "age" at "person"),
			Bind(3.pos, "address" at "person"),
			Bind(4.pos, "code" at "diagnosis"),
			Bind(5.pos, "name" at "diagnosis"),
			Bind(6.pos, "researchType" at "person"),
			Bind(7.pos, "address" at "lab"),
			Bind(8.pos, "name" at "lab"),
			Bind(9.pos, "code" at "lab"),
			Bind(10.pos, "dateStart" at "analysis"),
			Bind(11.pos, "timeStart" at "analysis"),
			Bind(12.pos, "dateComplete" at "analysis"),
			Bind(13.pos, "timeComplete" at "analysis")
		)
		converter.conversion.start()
		assertEquals("Иванов Иван Иванович", converter.schema.table["name" at "person", 1]!!.value.getString())
		assertEquals("21.06.1963", converter.schema.table["birthday" at "person", 1]!!.value.getString())
		assertEquals("13:53", converter.schema.table["timeComplete" at "analysis", 1]!!.value.getString())
	}
}