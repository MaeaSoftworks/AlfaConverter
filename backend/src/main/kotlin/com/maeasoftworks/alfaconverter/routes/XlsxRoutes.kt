package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.XlsxConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.models.XlsxPreviewResponse
import com.maeasoftworks.alfaconverter.services.FileType
import com.maeasoftworks.alfaconverter.utils.deserialize
import com.maeasoftworks.alfaconverter.utils.extractParts
import com.maeasoftworks.alfaconverter.utils.tryGetBytes
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.xlsxRouting() {
	route("/api/xlsx") {
		post("preview") {
			val params = call.receiveMultipart().extractParts("source", "target")
			val source = params["source"].tryGetBytes().also { FileType.validate(it, FileType.XLSX) }
			val target = params["target"].tryGetBytes().also { FileType.validate(it, FileType.XLSX) }
			val converter = XlsxConverter(source, target)
			val headers = converter.getHeaders()
			val examples = converter.getExamples()
			call.respond(listOf(
				XlsxPreviewResponse(headers.first, examples.first),
				XlsxPreviewResponse(headers.second, examples.second)
			))
		}

		post("/convert") {
			val params = call.receiveMultipart().extractParts("source", "target", "conversion")
			val source = params["source"].tryGetBytes().also { FileType.validate(it, FileType.XLSX) }
			val target = params["target"].tryGetBytes().also { FileType.validate(it, FileType.XLSX) }
			val conversion = params["conversion"].deserialize<Conversion>()!!
			call.respond(XlsxConverter(source, target, conversion).convert())
		}
	}
}