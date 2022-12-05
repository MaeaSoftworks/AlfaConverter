package com.maeasoftworks.alfaconverter.services

import com.maeasoftworks.alfaconverter.models.FileType
import io.ktor.server.plugins.*
import org.apache.tika.Tika

object FileValidator {
	private val tika = Tika()

	fun validate(file: ByteArray, awaitedFileType: FileType) {
		if (file.isEmpty()) throw BadRequestException("File was empty")
		if (FileType[tika.detect(file)] != awaitedFileType) throw BadRequestException("Incorrect file format")
	}
}