package com.maeasoftworks.alfaconverter.services

import com.maeasoftworks.alfaconverter.core.XlsxConverter
import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.dao.Log
import com.maeasoftworks.alfaconverter.dto.Lines
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class XlsxService(private val logger: Logger) {
	fun getHeaders(firstFile: MultipartFile, secondFile: MultipartFile) : List<Lines> {
		val result = XlsxConverter(firstFile.bytes, secondFile.bytes).getHeaders()
		return listOf(
			Lines(result[0], result[1]),
			Lines(result[2], result[3])
		)
	}

	fun convert(
		firstFile: MultipartFile,
		secondFile: MultipartFile,
		isInverted: Boolean,
		conversion: Conversion
	): ByteArray {
		val result = XlsxConverter(
			if (!isInverted) firstFile.bytes else secondFile.bytes,
			if (!isInverted) secondFile.bytes else firstFile.bytes,
			conversion = conversion
		).convert()
		logger.write(Log(LocalDateTime.now(), conversion, 0))
		return result
	}
}