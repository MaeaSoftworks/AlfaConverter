package com.maeasoftworks.alfaconverter.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHTTP() {
	install(DefaultHeaders)
	install(CORS) {
		allowMethod(HttpMethod.Put)
		anyHost()
	}
}
