package com.maeasoftworks.alfaconverter.services

import io.ktor.server.plugins.*
import org.apache.tika.Tika

enum class FileType(val tikaExtension: String) {
	XLSX("application/x-tika-ooxml"),
	XML("application/xml");

	companion object {
		private val map = FileType.values().associateBy(FileType::tikaExtension)
		private val tika = Tika()

		operator fun get(value: String) = map[value]

		fun validate(file: ByteArray, awaitedFileType: FileType) {
			if (file.isEmpty()) throw BadRequestException("File was empty")
			if (FileType[tika.detect(file)] != awaitedFileType) throw BadRequestException("Incorrect file format")
		}
	}
}