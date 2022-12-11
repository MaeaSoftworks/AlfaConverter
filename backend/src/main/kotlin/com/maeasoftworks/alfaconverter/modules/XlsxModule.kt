package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.models.XlsxPreviewResponse
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

fun Application.xlsxModule() {
	routing {
		route("/api/xlsx") {
			post("preview") {
				val params = call.receiveMultipart().extractParts("source", "modifier")
				val source = params["source"].asByteArray() require Extension.XLSX
				val target = params["modifier"].asByteArray() require Extension.XLSX
				val converter = Converter(source = Xlsx(source), modifier = Xlsx(target))
				call.respond(
					listOf(
						XlsxPreviewResponse(converter.source.getHeaders(), converter.source.getExamples()),
						XlsxPreviewResponse(converter.modifier!!.getHeaders())
					)
				)
			}

			post("/convert") {
				val params = call.receiveMultipart().extractParts("source", "modifier", "conversion")
				val source = params["source"].asByteArray() require Extension.XLSX
				val target = params["modifier"].asByteArray() require Extension.XLSX
				val conversion = params["conversion"].deserializeTo<Conversion>()
				val response = Converter(Xlsx(source), Xlsx(target), Xlsx(), conversion).convert()
				call.respondBytes(response, ContentType.Application.Xlsx)
			}
		}
	}
}