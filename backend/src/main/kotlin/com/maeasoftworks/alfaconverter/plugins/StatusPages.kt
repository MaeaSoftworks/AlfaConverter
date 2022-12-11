package com.maeasoftworks.alfaconverter.plugins

import com.maeasoftworks.alfaconverter.exceptions.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString

fun Application.configureStatusPages() {
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
}
