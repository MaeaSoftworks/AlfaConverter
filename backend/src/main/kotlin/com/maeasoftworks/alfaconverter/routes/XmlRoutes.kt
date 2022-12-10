package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.xml.XmlConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import com.maeasoftworks.alfaconverter.models.XmlPreviewResponse
import com.maeasoftworks.alfaconverter.services.Extension
import com.maeasoftworks.alfaconverter.services.require
import com.maeasoftworks.alfaconverter.utils.asByteArray
import com.maeasoftworks.alfaconverter.utils.deserializeTo
import com.maeasoftworks.alfaconverter.utils.extractParts
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.xmlRouting() {
	route("/api/xml") {
		post("preview") {
			val params = call.receiveMultipart().extractParts("xlsx", "xsd")
			val firstFile = params["xlsx"].asByteArray() require Extension.XLSX
			val secondFile = params["xsd"].asByteArray() require Extension.XML
			val xmlConverter = XmlConverter(firstFile, secondFile)
			val response = XmlPreviewResponse(
				xmlConverter.getHeaders(),
				xmlConverter.getExamples(),
				xmlConverter.getSchema(),
				xmlConverter.getEndpoints()
			)
			call.respond(response)
		}

		post("/convert") {
			val params = call.receiveMultipart().extractParts("xlsx", "schema", "conversion")
			val xlsx = params["xlsx"].asByteArray() require Extension.XLSX
			val schema = params["schema"].deserializeTo<Element>()
			val conversion = params["conversion"].deserializeTo<Conversion>()
			val response = XmlConverter(xlsx, schema, conversion).convert()
			call.respondText(response, ContentType.Text.Xml)
		}
	}
}