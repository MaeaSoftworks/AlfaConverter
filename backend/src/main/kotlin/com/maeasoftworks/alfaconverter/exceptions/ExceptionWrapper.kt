package com.maeasoftworks.alfaconverter.exceptions

import com.maeasoftworks.alfaconverter.plugins.serializer
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class ExceptionWrapper(val code: String, val message: String) {
    constructor(code: String, cause: Throwable) : this(code, cause.message ?: "No message provided")
}

inline fun <reified T : Throwable> StatusPagesConfig.register(code: HttpStatusCode) {
    exception<T> { call, cause ->
        call.respondText(
            serializer.encodeToString(ExceptionWrapper(code.value.toString(), cause)),
            ContentType.Application.Json,
            code
        )
    }
}
