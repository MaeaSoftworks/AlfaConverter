package com.maeasoftworks.alfaconverter.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	install(StatusPages) {
		status(HttpStatusCode.InternalServerError) { call, status ->
			call.respondText(text = "500", status = status)
		}
	}

	routing {
		xlsxRouting()
		xmlRouting()
	}
}
