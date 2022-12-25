package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.core.Bind
import com.maeasoftworks.alfaconverter.core.Conversion
import com.maeasoftworks.alfaconverter.core.Merge
import com.maeasoftworks.alfaconverter.core.Split
import com.maeasoftworks.alfaconverter.core.xml.structure.ComplexType
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import com.maeasoftworks.alfaconverter.core.xml.structure.Primitive
import com.maeasoftworks.alfaconverter.dto.XmlPreviewResponse
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class XmlModuleTest {
    private val element = Element(
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
    ).also { println("\u001B[33mSchema from api/xml/preview as JSON:\n\u001B[33m${serializer.encodeToString(it)}") }

    @Test
    fun `test post api-xml-preview`() = testApplication {
        environment { config = ApplicationConfig("application.conf") }
        client.post("/api/xml/preview") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            "source",
                            File("src/test/resources/modules/xml/source.xlsx").readBytes(),
                            Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Application.Xlsx)
                                append(HttpHeaders.ContentDisposition, "filename=\"source.xlsx\"")
                            }
                        )
                        append(
                            "modifier",
                            File("src/test/resources/modules/xml/modifier.xsd").readBytes(),
                            Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Application.Xml)
                                append(HttpHeaders.ContentDisposition, "filename=\"modifier.xsd\"")
                            }
                        )
                    }
                )
            )
        }.apply {
            val expected = serializer.encodeToString(
                XmlPreviewResponse(
                    listOf(
                        listOf("ФИО"),
                        listOf("Дата рождения"),
                        listOf("Возраст пациента"),
                        listOf("Адрес прописки пациента"),
                        listOf("Диагноз (код)"),
                        listOf("Диагноз (расшифровка)"),
                        listOf("Тип исследования"),
                        listOf("Адрес лаборатории"),
                        listOf("Название лаборатории"),
                        listOf("Код лаборатории"),
                        listOf("Дата взятия анализа"),
                        listOf("Время взятия анализа"),
                        listOf("Дата выполнения"),
                        listOf("Время выполнения анализа")
                    ),
                    listOf(
                        "Иванов Иван Иванович",
                        "21.06.1963",
                        "59",
                        "Псков, ул. Ленина 35-28",
                        "К29.3",
                        "Гастрит",
                        "Гематологическое исследование",
                        "Псков, 8 Марта, 23",
                        "Клинико-диагностический центр",
                        "145",
                        "22.06.2022",
                        "9:20:00",
                        "24.06.2022",
                        "13:53"
                    ),
                    element,
                    listOf(
                        listOf("root", "person", "firstname"),
                        listOf("root", "person", "lastname"),
                        listOf("root", "person", "middleName"),
                        listOf("root", "person", "birthday"),
                        listOf("root", "person", "age"),
                        listOf("root", "person", "address"),
                        listOf("root", "person", "diagnosis"),
                        listOf("root", "person", "researchType"),
                        listOf("root", "person", "lab", "address"),
                        listOf("root", "person", "lab", "name"),
                        listOf("root", "person", "lab", "code"),
                        listOf("root", "person", "analysis", "dateStart"),
                        listOf("root", "person", "analysis", "timeStart"),
                        listOf("root", "person", "analysis", "dateComplete"),
                        listOf("root", "person", "analysis", "timeComplete")
                    )
                )
            )
            assertEquals(200, status.value)
            assertEquals(expected, bodyAsText())
        }
    }

    @Test
    fun `test post api-xml-convert`() = testApplication {
        environment { config = ApplicationConfig("application.conf") }
        client.post("/api/xml/convert") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            "source",
                            File("src/test/resources/modules/xml/source.xlsx").readBytes(),
                            Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Application.Xlsx)
                                append(HttpHeaders.ContentDisposition, "filename=\"source.xlsx\"")
                            }
                        )
                        append(
                            "schema",
                            serializer.encodeToString(element),
                            Headers.build { append(HttpHeaders.ContentType, ContentType.Application.Json) }
                        )
                        append(
                            "conversion",
                            serializer.encodeToString(
                                Conversion(
                                    mutableListOf(
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
                                            "[\${0}] \${1}"
                                        ),
                                        Bind(listOf("Тип исследования"), listOf("root", "person", "researchType")),
                                        Bind(listOf("Адрес лаборатории"), listOf("root", "person", "lab", "address")),
                                        Bind(listOf("Название лаборатории"), listOf("root", "person", "lab", "name")),
                                        Bind(listOf("Код лаборатории"), listOf("root", "person", "lab", "code")),
                                        Bind(
                                            listOf("Дата взятия анализа"),
                                            listOf("root", "person", "analysis", "dateStart")
                                        ),
                                        Bind(
                                            listOf("Время взятия анализа"),
                                            listOf("root", "person", "analysis", "timeStart")
                                        ),
                                        Bind(
                                            listOf("Дата выполнения"),
                                            listOf("root", "person", "analysis", "dateComplete")
                                        ),
                                        Bind(
                                            listOf("Время выполнения анализа"),
                                            listOf("root", "person", "analysis", "timeComplete")
                                        )
                                    )
                                )
                            ).also { println("\u001B[33mConversion from api/xml/convert as JSON:\n\u001B[33m$it") },
                            Headers.build { append(HttpHeaders.ContentType, ContentType.Application.Json) }
                        )
                    }
                )
            )
        }.apply {
            assertEquals(
                "<root><person><firstname>Иван</firstname><lastname>Иванов</lastname><middleName>Иванович</middleName><birthday>21.06.1963</birthday><age>59</age><address>Псков, ул. Ленина 35-28</address><diagnosis>[К29.3] Гастрит</diagnosis><researchType>Гематологическое исследование</researchType><lab address=\"Псков, 8 Марта, 23\" name=\"Клинико-диагностический центр\" code=145/><analysis dateStart=\"22.06.2022\" timeStart=\"9:20:00\" dateComplete=\"24.06.2022\" timeComplete=\"13:53\"/></person></root>",
                bodyAsText()
            )
        }
    }
}
