package com.maeasoftworks.alfaconverter.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import com.maeasoftworks.alfaconverter.dao.Log
import com.maeasoftworks.alfaconverter.dto.XmlHeadersResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class XmlService(
	private val logger: Logger,
	private val mapper: ObjectMapper
) {
	fun getHeadersAndSchema(file: MultipartFile, xsd: MultipartFile): XmlHeadersResponse {
		val xmlConverter = XmlConverter(file.bytes, xsd.bytes)
		val result = XmlHeadersResponse()
		val data = xmlConverter.getHeadersAndFirstLine()
		result.headers = data[0]
		result.firstLine = data[1]
		result.schema = mapper.readTree(xmlConverter.getSchema())
		return result
	}

	fun convert(file: MultipartFile, schema: List<Element>): String {
		val xmlConverter = XmlConverter(file.bytes, schema)
		val result = xmlConverter.convert()
		logger.write(Log(LocalDateTime.now(), "", 0))
		return result
	}
}