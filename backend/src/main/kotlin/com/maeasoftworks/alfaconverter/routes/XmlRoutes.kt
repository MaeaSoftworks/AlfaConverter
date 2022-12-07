package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import com.maeasoftworks.alfaconverter.models.XmlPreviewResponse
import com.maeasoftworks.alfaconverter.services.FileType
import com.maeasoftworks.alfaconverter.utils.deserialize
import com.maeasoftworks.alfaconverter.utils.extractParts
import com.maeasoftworks.alfaconverter.utils.tryGetBytes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.xmlRouting() {
	route("/api/xml") {
		post("preview") {
			val params = call.receiveMultipart().extractParts("xlsx", "xsd")
			val firstFile = params["xlsx"].tryGetBytes().also { FileType.validate(it, FileType.XLSX) }
			val secondFile = params["xsd"].tryGetBytes().also { FileType.validate(it, FileType.XML) }
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
			val xlsx = params["xlsx"].tryGetBytes().also { FileType.validate(it, FileType.XLSX) }
			val schema = params["schema"].deserialize<Element>()!!
			val conversion = params["conversion"].deserialize<Conversion>()
			val response = XmlConverter(xlsx, schema, conversion = conversion!!).convert()
			call.respondText(response, ContentType.Text.Xml)
		}
	}
}