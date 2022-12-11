package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.core.xml.Xml
import com.maeasoftworks.alfaconverter.core.xml.Xsd
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

fun Application.xmlModule()  {
	routing {
		route("/api/xml") {
			post("preview") {
				val params = call.receiveMultipart().extractParts("source", "modifier")
				val source = params["source"].asByteArray() require Extension.XLSX
				val modifier = params["modifier"].asByteArray() require Extension.XML
				val xmlConverter = Converter(Xlsx(source), Xsd(modifier), null)
				val response = XmlPreviewResponse(
					xmlConverter.source.getHeaders(),
					xmlConverter.source.getExamples(),
					xmlConverter.modifier.getAdditionalData(),
					xmlConverter.modifier.getHeaders()
				)
				call.respond(response)
			}

			post("/convert") {
				val params = call.receiveMultipart().extractParts("source", "schema", "conversion")
				val source = params["source"].asByteArray() require Extension.XLSX
				val schema = params["schema"].deserializeTo<Element>()
				val conversion = params["conversion"].deserializeTo<Conversion>()
				val response = Converter(
					source = Xlsx(source),
					result = Xml(schema),
					conversion = conversion
				).convert()
				call.respondText(String(response), ContentType.Text.Xml)
			}
		}
	}
}