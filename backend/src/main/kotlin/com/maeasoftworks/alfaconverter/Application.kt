package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.modules.jsonModule
import com.maeasoftworks.alfaconverter.modules.xlsxModule
import com.maeasoftworks.alfaconverter.modules.xmlModule
import com.maeasoftworks.alfaconverter.plugins.configureHTTP
import com.maeasoftworks.alfaconverter.plugins.configureLogging
import com.maeasoftworks.alfaconverter.plugins.configureSerialization
import com.maeasoftworks.alfaconverter.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
	configureSerialization()
	configureLogging()
	configureHTTP()
	configureStatusPages()
	xlsxModule()
	xmlModule()
	jsonModule()
}
