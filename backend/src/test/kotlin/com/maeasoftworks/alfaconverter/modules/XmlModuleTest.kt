package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.core.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.core.conversions.actions.Split
import com.maeasoftworks.alfaconverter.core.xml.structure.ComplexType
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import com.maeasoftworks.alfaconverter.core.xml.structure.Primitive
import com.maeasoftworks.alfaconverter.core.xml.structure.invoke
import com.maeasoftworks.alfaconverter.models.XmlPreviewResponse
import com.maeasoftworks.alfaconverter.plugins.serializer

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
	private val element = Element("root").also { root ->
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
	}.also { println("\u001B[33mSchema from api/xml/preview as JSON:\n\u001B[33m${serializer.encodeToString(it)}") }

	@Test
	fun `test post api-xml-preview`() = testApplication {
		environment { config = ApplicationConfig("application-xml-tests.conf") }
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
						"ФИО",
						"Дата рождения",
						"Возраст пациента",
						"Адрес прописки пациента",
						"Диагноз (код)",
						"Диагноз (расшифровка)",
						"Тип исследования",
						"Адрес лаборатории",
						"Название лаборатории",
						"Код лаборатории",
						"Дата взятия анализа",
						"Время взятия анализа",
						"Дата выполнения",
						"Время выполнения анализа"
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
						"root.person.firstname",
						"root.person.lastname",
						"root.person.middleName",
						"root.person.birthday",
						"root.person.age",
						"root.person.address",
						"root.person.diagnosis",
						"root.person.researchType",
						"root.person.lab.address",
						"root.person.lab.name",
						"root.person.lab.code",
						"root.person.analysis.dateStart",
						"root.person.analysis.timeStart",
						"root.person.analysis.dateComplete",
						"root.person.analysis.timeComplete"
					)
				)
			)
			assertEquals(200, status.value)
			assertEquals(expected, bodyAsText())
		}
	}

	@Test
	fun `test post api-xml-convert`() = testApplication {
		environment { config = ApplicationConfig("application-xml-tests.conf") }
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
								)
							).also { println("\u001B[33mConversion from api/xml/convert as JSON:\n\u001B[33m${it}") },
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