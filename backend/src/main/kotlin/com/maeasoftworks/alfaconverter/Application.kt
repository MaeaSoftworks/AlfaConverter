package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.plugins.configureHTTP
import com.maeasoftworks.alfaconverter.plugins.configureLogging
import com.maeasoftworks.alfaconverter.routes.configureRouting
import com.maeasoftworks.alfaconverter.plugins.configureSerialization
import io.ktor.server.application.*
import com.maeasoftworks.alfaconverter.plugins.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSerialization()
    configureLogging()
    configureHTTP()
    configureRouting()
}
