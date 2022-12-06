package com.maeasoftworks.alfaconverter.plugins

import io.ktor.server.plugins.defaultheaders.*
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    install(DefaultHeaders)
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        anyHost()
    }
}
