package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.xlsx.XlsxConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.models.XlsxPreviewResponse
import com.maeasoftworks.alfaconverter.services.Extension
import com.maeasoftworks.alfaconverter.services.require
import com.maeasoftworks.alfaconverter.utils.asByteArray
import com.maeasoftworks.alfaconverter.utils.deserializeTo
import com.maeasoftworks.alfaconverter.utils.extractParts
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.xlsxRouting() {
	route("/api/xlsx") {
		post("preview") {
			val params = call.receiveMultipart().extractParts("source", "target")
			val source = params["source"].asByteArray() require Extension.XLSX
			val target = params["target"].asByteArray() require Extension.XLSX
			val converter = XlsxConverter(source, target)
			val headers = converter.getHeaders()
			val examples = converter.getExamples()
			call.respond(
				listOf(
					XlsxPreviewResponse(headers.first, examples.first),
					XlsxPreviewResponse(headers.second, examples.second)
				)
			)
		}

		post("/convert") {
			val params = call.receiveMultipart().extractParts("source", "target", "conversion")
			val source = params["source"].asByteArray() require Extension.XLSX
			val target = params["target"].asByteArray() require Extension.XLSX
			val conversion = params["conversion"].deserializeTo<Conversion>()
			call.respond(XlsxConverter(source, target, conversion).convert())
		}
	}
}