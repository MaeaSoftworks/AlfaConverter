package com.maeasoftworks.alfaconverter.routes

import com.maeasoftworks.alfaconverter.core.XlsxConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.models.FileType
import com.maeasoftworks.alfaconverter.models.Lines
import com.maeasoftworks.alfaconverter.services.FileValidator
import com.maeasoftworks.alfaconverter.utils.deserialize
import com.maeasoftworks.alfaconverter.utils.extractParts
import com.maeasoftworks.alfaconverter.utils.tryGetBytes
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.xlsxRouting() {
	route("/api/xlsx") {
		post("headers") {
			val formParameters = call.receiveMultipart()
			val files = formParameters.extractParts("first-file", "second-file")
			val firstFile = files["first-file"].tryGetBytes().also { FileValidator.validate(it, FileType.XLSX) }
			val secondFile = files["second-file"].tryGetBytes().also { FileValidator.validate(it, FileType.XLSX) }
			val result = XlsxConverter(firstFile, secondFile).getHeaders()
			call.respond(
				listOf(
					Lines(result[0], result[1]),
					Lines(result[2], result[3])
				)
			)
		}

		post("/convert") {
			val formParameters = call.receiveMultipart()
			val params = formParameters.extractParts("first-file", "second-file", "conversion")
			val firstFile = params["first-file"].tryGetBytes().also { FileValidator.validate(it, FileType.XLSX) }
			val secondFile = params["second-file"].tryGetBytes().also { FileValidator.validate(it, FileType.XLSX) }
			val conversion = params["conversion"].deserialize<Conversion>()
			call.respond(XlsxConverter(firstFile, secondFile, conversion = conversion!!).convert())
		}
	}
}