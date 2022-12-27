package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.core.Bind
import com.maeasoftworks.alfaconverter.core.Conversion
import com.maeasoftworks.alfaconverter.core.Merge
import com.maeasoftworks.alfaconverter.core.Split
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import java.io.ByteArrayInputStream

class XlsxRoutesTest : FunSpec() {
    init {
        test("[POST] api/xlsx/preview") {
            testApplication {
                environment { config = ApplicationConfig("application.conf") }
                client.post("/api/xlsx/preview") {
                    setBody(MultiPartFormDataContent(formData {
                        appendFile("source", "src/test/resources/routes/xlsx", "source.xlsx", ContentType.Application.Xlsx)
                        appendFile("modifier", "src/test/resources/routes/xlsx", "modifier.xlsx", ContentType.Application.Xlsx)
                    }))
                }.bodyAsText() shouldBe """
                    [{"headers":[["Column to bind 1"],["Column to bind 2"],["Will be split"],["Will"],["be"],["merged"],["who?"]],"examples":["10","100","a b c","1","2","3","me"]},{"headers":[["bind 2 here"],["bind 1 here"],["was"],["split"],["!!!"],["merged"],["whoooo?"]]}]
                """.trimIndent()
            }
        }

        test("[POST] api/xlsx/convert") {
            testApplication {
                environment { config = ApplicationConfig("application.conf") }
                val response = client.post("/api/xlsx/convert") {
                    setBody(MultiPartFormDataContent(formData {
                        appendFile("source", "src/test/resources/routes/xlsx", "source.xlsx", ContentType.Application.Xlsx)
                        appendFile("modifier", "src/test/resources/routes/xlsx", "modifier.xlsx", ContentType.Application.Xlsx)
                        append("conversion", createAndPrintConversion())
                    }))
                }
                with(
                    (SpreadsheetMLPackage.load(ByteArrayInputStream(response.body())).`package`.parts[PartName("/xl/worksheets/sheet1.xml")] as WorksheetPart)
                        .contents.sheetData
                ) {
                    row.size shouldBe 10
                    for (row in row) {
                        row.c.size shouldBe 7
                    }
                    row[9].c[5].`is`.t.value shouldBe "1 2 3"
                }
            }
        }
    }

    private fun createAndPrintConversion(): String {
        return serializer.encodeToString(
            Conversion(
                mutableListOf(
                    Bind(listOf("Column to bind 1"), listOf("bind 1 here")),
                    Bind(listOf("Column to bind 2"), listOf("bind 2 here")),
                    Split(listOf("Will be split"), listOf(listOf("was"), listOf("split"), listOf("!!!")), "(\\S+) (\\S+) (\\S+)"),
                    Merge(listOf(listOf("Will"), listOf("be"), listOf("merged")), listOf("merged"), "\${0} \${1} \${2}"),
                    Bind(listOf("who?"), listOf("whoooo?"))
                )
            )
        ).also { println("\u001B[33mConversion from api/xlsx/convert as JSON:\n\u001B[33m$it") }
    }
}
