package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.modules.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureLogging()
    configureCORS()
    configureHeaders()
    configureExceptionsHandler()
    configureSerialization()
    configureRouting()
}
