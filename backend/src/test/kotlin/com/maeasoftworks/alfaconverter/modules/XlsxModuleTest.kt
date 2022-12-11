@file:Suppress("SpellCheckingInspection")

package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.conversions.actions.*
import com.maeasoftworks.alfaconverter.core.xlsx.structure.TypeName
import com.maeasoftworks.alfaconverter.models.XlsxPreviewResponse
import com.maeasoftworks.alfaconverter.plugins.serializer
import io.ktor.client.call.*

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class XlsxModuleTest {

	@Test
	fun `test post api-xlsx-preview`() = testApplication {
		environment { config = ApplicationConfig("application-xlsx-tests.conf") }
		client.post("/api/xlsx/preview") {
			setBody(
				MultiPartFormDataContent(
					formData {
						append(
							"source",
							File("src/test/resources/modules/xlsx/source.xlsx").readBytes(),
							Headers.build {
								append(HttpHeaders.ContentType, ContentType.Application.Xlsx)
								append(HttpHeaders.ContentDisposition, "filename=\"source.xlsx\"")
							}
						)
						append(
							"modifier",
							File("src/test/resources/modules/xlsx/modifier.xlsx").readBytes(),
							Headers.build {
								append(HttpHeaders.ContentType, ContentType.Application.Xlsx)
								append(HttpHeaders.ContentDisposition, "filename=\"modifier.xlsx\"")
							}
						)
					}
				)
			)
		}.apply {
			val body = serializer.decodeFromString<List<XlsxPreviewResponse>>(bodyAsText())
			val expected = listOf(
				XlsxPreviewResponse(
					listOf("Column to bind 1", "Column to bind 2", "Will be split", "Will", "be", "merged", "who?"),
					listOf("10", "100", "a b c", "1", "2", "3", "me")
				),
				XlsxPreviewResponse(listOf("bind 2 here", "bind 1 here", "was", "split", "!!!", "merged", "whoooo?"))
			)
			assertContentEquals(expected, body)
		}
	}

	@Test
	fun `test post api-xlsx-convert`() = testApplication {
		environment { config = ApplicationConfig("application-xlsx-tests.conf") }
		client.post("/api/xlsx/convert") {
			setBody(
				MultiPartFormDataContent(
					formData {
						append(
							"source",
							File("src/test/resources/modules/xlsx/source.xlsx").readBytes(),
							Headers.build {
								append(HttpHeaders.ContentType, ContentType.Application.Xlsx)
								append(HttpHeaders.ContentDisposition, "filename=\"source.xlsx\"")
							}
						)
						append(
							"modifier",
							File("src/test/resources/modules/xlsx/modifier.xlsx").readBytes(),
							Headers.build {
								append(HttpHeaders.ContentType, ContentType.Application.Xlsx)
								append(HttpHeaders.ContentDisposition, "filename=\"modifier.xlsx\"")
							}
						)
						append(
							"conversion",
							serializer.encodeToString(
								Conversion(
									mutableListOf(
										Bind("Column to bind 1", "bind 1 here"),
										Bind("Column to bind 2", "bind 2 here"),
										Split("Will be split", listOf("was", "split", "!!!"), "(\\S+) (\\S+) (\\S+)"),
										Merge(listOf("Will", "be", "merged"), "merged", "\${Will} \${be} \${merged}"),
										Bind("who?", "whoooo?"),
										Cast("bind 1 here", TypeName.Number, 0)
									)
								)
							).also { println("\u001B[33mConversion from api/xlsx/convert as JSON:\n\u001B[33m${it}") }
						)
					}
				)
			)
		}.apply {
			val sheet = (SpreadsheetMLPackage.load(ByteArrayInputStream(body()))
				.`package`.parts[PartName("/xl/worksheets/sheet1.xml")] as WorksheetPart)
				.contents.sheetData
			assertEquals(10, sheet.row.size)
			for (row in sheet.row) {
				assertEquals(7, row.c.size)
			}
			assertEquals("1 2 3", sheet.row[9].c[5].`is`.t.value)
		}
	}
}