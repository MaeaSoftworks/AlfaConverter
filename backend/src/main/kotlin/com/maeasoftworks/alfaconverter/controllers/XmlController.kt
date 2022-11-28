package com.maeasoftworks.alfaconverter.controllers

import com.maeasoftworks.alfaconverter.services.XmlService
import com.maeasoftworks.alfaconverter.validators.FileNotEmpty
import com.maeasoftworks.alfaconverter.validators.MustBeXlsx
import com.maeasoftworks.alfaconverter.validators.MustBeXsdOrXml
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
		@RequestParam("first-file")     @FileNotEmpty   @MustBeXlsx     firstFile: MultipartFile,
		@RequestParam("second-file")    @FileNotEmpty   @MustBeXsdOrXml secondFile: MultipartFile
	) = xmlService.getHeadersAndSchema(firstFile, secondFile)


	@PostMapping("convert", produces = ["application/xml"])
	fun convert(
		@RequestParam("file")   @MustBeXlsx     @FileNotEmpty   file: MultipartFile,
		@RequestParam("xsd")    @MustBeXsdOrXml @FileNotEmpty   xsd: MultipartFile
	): String = xmlService.convert(file, xsd)
}