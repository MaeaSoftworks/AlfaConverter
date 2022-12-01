package com.maeasoftworks.alfaconverter.controllers

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.extensions.toBoolean
import com.maeasoftworks.alfaconverter.validators.FileNotEmpty
import com.maeasoftworks.alfaconverter.services.XlsxService
import com.maeasoftworks.alfaconverter.validators.MustBeXlsx
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/xlsx")
@CrossOrigin
@Validated
class XlsxController(private val xlsxService: XlsxService) {
	@PostMapping("headers", produces = ["application/json"])
	@ResponseBody
	fun getHeaders(
		@RequestParam("first-file")     @FileNotEmpty   @MustBeXlsx firstFile: MultipartFile,
		@RequestParam("second-file")    @FileNotEmpty   @MustBeXlsx secondFile: MultipartFile
	) = xlsxService.getHeaders(firstFile, secondFile)

	@PostMapping("convert")
	fun convert(
		@RequestParam("first-file")     @FileNotEmpty   @MustBeXlsx firstFile: MultipartFile,
		@RequestParam("second-file")    @FileNotEmpty   @MustBeXlsx secondFile: MultipartFile,
		@RequestParam("headship", required = false)                 headship: Int = 0,
		@RequestParam("conversion", required = false)               conversion: String = ""
	): ResponseEntity<ByteArrayResource> = ResponseEntity
		.ok()
		.headers(
			HttpHeaders().also {
				it.contentType = MediaType.APPLICATION_OCTET_STREAM
				it[HttpHeaders.CONTENT_DISPOSITION] = "attachment; filename=${firstFile.originalFilename} + ${secondFile.originalFilename}.xlsx"
			}
		)
		.body(
			ByteArrayResource(
				xlsxService.convert(
					firstFile,
					secondFile,
					headship.toBoolean(),
					Json.decodeFromString(conversion) ?: Conversion.empty
				)
			)
		)
}