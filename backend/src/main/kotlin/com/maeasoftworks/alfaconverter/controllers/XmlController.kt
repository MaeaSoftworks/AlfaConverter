package com.maeasoftworks.alfaconverter.controllers

import com.maeasoftworks.alfaconverter.services.XmlService
import com.maeasoftworks.alfaconverter.validators.FileNotEmpty
import com.maeasoftworks.alfaconverter.validators.MustBeXlsx
import com.maeasoftworks.alfaconverter.validators.MustBeXsdOrXml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/xml")
@CrossOrigin
@Validated
class XmlController(private val xmlService: XmlService) {
	@PostMapping("headers", produces = ["application/json"])
	@ResponseBody
	fun getHeaders(
		@RequestParam("xlsx")   @FileNotEmpty   @MustBeXlsx     xlsx: MultipartFile,
		@RequestParam("xsd")    @FileNotEmpty   @MustBeXsdOrXml xsd: MultipartFile
	) = xmlService.getHeadersAndSchema(xlsx, xsd)

	@PostMapping("convert", produces = ["application/xml"], consumes = ["multipart/form-data"])
	fun convert(
		@RequestParam("xlsx")   @FileNotEmpty   @MustBeXlsx     xlsx: MultipartFile,
		@RequestParam("schema")                                 schema: String,
		@RequestParam("conversion", required = false)           conversion: String = ""
	) = xmlService.convert(xlsx, Json.decodeFromString(schema))
}