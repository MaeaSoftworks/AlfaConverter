package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.Bind
import com.maeasoftworks.alfaconverter.core.Merge
import com.maeasoftworks.alfaconverter.core.Split
import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.core.xml.Schema
import com.maeasoftworks.alfaconverter.core.xml.Xml
import com.maeasoftworks.alfaconverter.core.xml.structure.ComplexType
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import com.maeasoftworks.alfaconverter.core.xml.structure.Primitive
import com.maeasoftworks.alfaconverter.modules.serializer
import kotlinx.serialization.encodeToString
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class XmlTests {
    private val schema = Schema(File("src/test/resources/xml/modifier.xsd").readText())

    private val converter = Converter(
        source = Xlsx(File("src/test/resources/xml/source.xlsx").readBytes()),
        result = Xml(
            Element(
                "root",
                ComplexType("root").also { rootType ->
                    rootType.fields["person"] = ComplexType("person").also { person ->
                        person.fields["firstname"] = Primitive.String
                        person.fields["lastname"] = Primitive.String
                        person.fields["middleName"] = Primitive.String
                        person.fields["birthday"] = Primitive.Date
                        person.fields["age"] = Primitive.Decimal
                        person.fields["address"] = Primitive.String
                        person.fields["diagnosis"] = Primitive.String
                        person.fields["researchType"] = Primitive.String
                        person.fields["lab"] = ComplexType("lab").also { lab ->
                            lab.attributes["address"] = Primitive.String
                            lab.attributes["name"] = Primitive.String
                            lab.attributes["code"] = Primitive.String
                        }
                        person.fields["analysis"] = ComplexType("analysis").also { analysis ->
                            analysis.attributes["dateStart"] = Primitive.Date
                            analysis.attributes["timeStart"] = Primitive.Time
                            analysis.attributes["dateComplete"] = Primitive.Date
                            analysis.attributes["timeComplete"] = Primitive.Time
                        }
                    }
                }
            )
        )
    )

    @Test
    fun `all types detected`() {
        assertEquals(5, schema.types.size, "Expected 4 types")
    }

    @Test
    fun `conversion test`() {
        converter.conversion.actions += mutableListOf(
            Split(
                listOf("ФИО"),
                listOf(
                    listOf("root", "person", "lastname"),
                    listOf("root", "person", "firstname"),
                    listOf("root", "person", "middleName")
                ),
                "(\\S+) (\\S+) (\\S+)"
            ),
            Bind(listOf("Дата рождения"), listOf("root", "person", "birthday")),
            Bind(listOf("Возраст пациента"), listOf("root", "person", "age")),
            Bind(listOf("Адрес прописки пациента"), listOf("root", "person", "address")),
            Merge(
                listOf(listOf("Диагноз (код)"), listOf("Диагноз (расшифровка)")),
                listOf("root", "person", "diagnosis"),
                "[\${Диагноз (код)}] \${Диагноз (расшифровка)}"
            ),
            Bind(listOf("Тип исследования"), listOf("root", "person", "researchType")),
            Bind(listOf("Адрес лаборатории"), listOf("root", "person", "lab", "address")),
            Bind(listOf("Название лаборатории"), listOf("root", "person", "lab", "name")),
            Bind(listOf("Код лаборатории"), listOf("root", "person", "lab", "code")),
            Bind(listOf("Дата взятия анализа"), listOf("root", "person", "analysis", "dateStart")),
            Bind(listOf("Время взятия анализа"), listOf("root", "person", "analysis", "timeStart")),
            Bind(listOf("Дата выполнения"), listOf("root", "person", "analysis", "dateComplete")),
            Bind(listOf("Время выполнения анализа"), listOf("root", "person", "analysis", "timeComplete"))
        )
        println(serializer.encodeToString(converter.conversion))
        converter.executeActions()
        assertEquals("Иван", converter.result.table[listOf("root", "person", "firstname"), 0]!!.getString())
        assertEquals("21.06.1963", converter.result.table[listOf("root", "person", "birthday"), 0]!!.getString())
        assertEquals(
            "13:53",
            converter.result.table[listOf("root", "person", "analysis", "timeComplete"), 0]!!.getString()
        )
    }
}
