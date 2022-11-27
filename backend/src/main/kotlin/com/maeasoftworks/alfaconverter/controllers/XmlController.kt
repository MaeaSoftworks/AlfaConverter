package com.maeasoftworks.alfaconverter.controllers

import com.maeasoftworks.alfaconverter.services.XmlService
import com.maeasoftworks.alfaconverter.validators.FileNotEmpty
import com.maeasoftworks.alfaconverter.validators.MustBeXlsx
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
	@PostMapping("convert")
	fun convert(
		@RequestParam("file") @MustBeXlsx @FileNotEmpty file: MultipartFile,
		@RequestParam("xsd") @MustBeXlsx @FileNotEmpty xsd: MultipartFile
	): ResponseEntity<ByteArrayResource> = ResponseEntity
		.ok()
		.headers(
			HttpHeaders().also {
				it.contentType = MediaType.APPLICATION_OCTET_STREAM
				it[HttpHeaders.CONTENT_DISPOSITION] = "attachment; filename=${file.originalFilename}.xml"
			}
		)
		.body(ByteArrayResource(xmlService.convert(file, xsd)))
}