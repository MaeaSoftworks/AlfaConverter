package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.Conversion
import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.dto.XlsxPreviewResponse
import com.maeasoftworks.alfaconverter.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.xlsxRoutes() {
    route("/api/xlsx") {
        post("preview") {
            val (source, target) = call.receiveMultipart().extractParts(
                "source" to { it.asByteArray() with Extension.XLSX },
                "modifier" to { it.asByteArray() with Extension.XLSX }
            )
            val converter = Converter(source = Xlsx(source), modifier = Xlsx(target))
            call.respond(
                listOf(
                    XlsxPreviewResponse(converter.source.getHeaders(), converter.source.getExamples()),
                    XlsxPreviewResponse(converter.modifier.getHeaders())
                )
            )
        }

        post("/convert") {
            val (source, modifier, conversion) = call.receiveMultipart().extractParts(
                "source" to { it.asByteArray() with Extension.XLSX },
                "modifier" to { it.asByteArray() with Extension.XLSX },
                "conversion" to { it.deserializeTo<Conversion>() }
            )
            val response = Converter(Xlsx(source), Xlsx(modifier), Xlsx(), conversion).convert()
            call.respondBytes(response, ContentType.Application.Xlsx)
        }
    }
}
