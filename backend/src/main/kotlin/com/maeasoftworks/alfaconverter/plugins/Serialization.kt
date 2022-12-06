package com.maeasoftworks.alfaconverter.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

val serializer = Json { allowStructuredMapKeys = true }

fun Application.configureSerialization() {
	install(ContentNegotiation) { json(serializer) }
}
