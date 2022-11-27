package com.maeasoftworks.alfaconverter.services

import com.maeasoftworks.alfaconverter.core.XmlConverter
import com.maeasoftworks.alfaconverter.dao.Log
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class XmlService(private val logger: Logger) {
	fun convert(file: MultipartFile, xsd: MultipartFile): ByteArray {
		val xmlConverter = XmlConverter(file.bytes, xsd.bytes)
		val result = xmlConverter.convert()
		logger.write(Log(LocalDateTime.now(), "", 0))
		return result
	}
}