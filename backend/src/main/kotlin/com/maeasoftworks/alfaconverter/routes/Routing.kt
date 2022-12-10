package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.exceptions.*
import com.maeasoftworks.alfaconverter.plugins.serializer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString

fun Application.configureRouting() {
	install(StatusPages) {
		register<InvalidPartDataTypeException>(HttpStatusCode.BadRequest)
		register<RequiredPartNotFoundException>(HttpStatusCode.BadRequest)
		register<IncorrectFileException>(HttpStatusCode.BadRequest)
		status(HttpStatusCode.NotFound) { call, code ->
			call.respondText(
				serializer.encodeToString(ExceptionWrapper("404", "Route not found")),
				ContentType.Application.Json,
				code
			)
		}
	}

	routing {
		xlsxRouting()
		xmlRouting()
	}
}
