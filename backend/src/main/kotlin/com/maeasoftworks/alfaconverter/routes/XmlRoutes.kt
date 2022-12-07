package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import com.maeasoftworks.alfaconverter.models.FileType
import com.maeasoftworks.alfaconverter.models.XmlHeadersResponse
import com.maeasoftworks.alfaconverter.services.FileValidator
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
		post("headers") {
			val formParameters = call.receiveMultipart()
			val files = formParameters.extractParts("xlsx", "xsd")
			val firstFile = files["xlsx"].tryGetBytes().also { FileValidator.validate(it, FileType.XLSX) }
			val secondFile = files["xsd"].tryGetBytes().also { FileValidator.validate(it, FileType.XML) }
			val xmlConverter = XmlConverter(firstFile, secondFile)
			val result = XmlHeadersResponse()
			val data = xmlConverter.getHeadersAndFirstLine()
			result.headers = data[0]
			result.firstLine = data[1]
			result.schema = xmlConverter.getSchema()
			result.endpoints = xmlConverter.getEndpoints()
			call.respond(result)
		}

		post("/convert") {
			val formParameters = call.receiveMultipart()
			val params = formParameters.extractParts("xlsx", "schema", "conversion")
			val xlsx = params["xlsx"].tryGetBytes().also { FileValidator.validate(it, FileType.XLSX) }
			val schema = params["schema"].deserialize<Element>()!!
			val conversion = params["conversion"].deserialize<Conversion>()
			call.respondText(XmlConverter(xlsx, schema, conversion = conversion!!).convert(), ContentType.Text.Xml)
		}
	}
}