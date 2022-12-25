package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.Conversion
import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.core.xml.Xml
import com.maeasoftworks.alfaconverter.core.xml.Xsd
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import com.maeasoftworks.alfaconverter.dto.XmlPreviewResponse
import com.maeasoftworks.alfaconverter.utils.Extension
import com.maeasoftworks.alfaconverter.utils.with
import com.maeasoftworks.alfaconverter.utils.asByteArray
import com.maeasoftworks.alfaconverter.utils.deserializeTo
import com.maeasoftworks.alfaconverter.utils.extractParts
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.xmlRoutes() {
    route("/api/xml") {
        post("preview") {
            val (source, modifier) = call.receiveMultipart().extractParts(
                "source" to { it.asByteArray() with Extension.XLSX },
                "modifier" to { it.asByteArray() with Extension.XML }
            )
            val xmlConverter = Converter(Xlsx(source), Xsd(modifier))
            val response = XmlPreviewResponse(
                xmlConverter.source.getHeaders(),
                xmlConverter.source.getExamples(),
                xmlConverter.modifier.getPayload(),
                xmlConverter.modifier.getHeaders()
            )
            call.respond(response)
        }

        post("/convert") {
            val (source, schema, conversion) = call.receiveMultipart().extractParts(
                "source" to { it.asByteArray() with Extension.XLSX },
                "schema" to { it.deserializeTo<Element>() },
                "conversion" to { it.deserializeTo<Conversion>() }
            )
            val response = Converter(Xlsx(source), Xml(schema), conversion).convert()
            call.respondText(String(response), ContentType.Text.Xml)
        }
    }
}
