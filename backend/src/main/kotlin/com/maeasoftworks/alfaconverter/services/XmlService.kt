package com.maeasoftworks.alfaconverter.services

import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.dao.Log
import com.maeasoftworks.alfaconverter.dto.XmlHeadersResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class XmlService(private val logger: Logger) {
	fun getHeadersAndSchema(file: MultipartFile, xsd: MultipartFile): XmlHeadersResponse {
		val xmlConverter = XmlConverter(file.bytes, xsd.bytes)
		val result = XmlHeadersResponse()
		val data = xmlConverter.getHeadersAndFirstLine()
		result.headers = data[0]
		result.firstLine = data[1]
		result.schema = xmlConverter.getSchema()
		return result
	}

	fun convert(file: MultipartFile, xsd: MultipartFile): String {
		val xmlConverter = XmlConverter(file.bytes, xsd.bytes)
		val result = xmlConverter.convert()
		logger.write(Log(LocalDateTime.now(), "", 0))
		return result
	}
}