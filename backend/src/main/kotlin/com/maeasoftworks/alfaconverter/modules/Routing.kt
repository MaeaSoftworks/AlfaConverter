package com.maeasoftworks.alfaconverter.modules

import com.maeasoftworks.alfaconverter.routes.jsonRouting
import com.maeasoftworks.alfaconverter.routes.xlsxRoutes
import com.maeasoftworks.alfaconverter.routes.xmlRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        xlsxRoutes()
        xmlRoutes()
        jsonRouting()
    }
}