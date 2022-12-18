package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.json.Json
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import com.maeasoftworks.alfaconverter.services.Extension
import com.maeasoftworks.alfaconverter.services.with
import com.maeasoftworks.alfaconverter.utils.asByteArray
import com.maeasoftworks.alfaconverter.utils.extractParts
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.jsonModule() {
	routing {
		route("/api/json") {
			post("/convert") {
				val source = call.receiveMultipart().extractParts("source" to { it.asByteArray() with Extension.XLSX })
				val response = Converter(Xlsx(source), Json()).convert()
				call.respondBytes(response, ContentType.Application.Json)
			}
		}
	}
}