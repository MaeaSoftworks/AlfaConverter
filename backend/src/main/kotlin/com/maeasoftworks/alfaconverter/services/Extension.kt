package com.maeasoftworks.alfaconverter.services

import com.maeasoftworks.alfaconverter.exceptions.IncorrectFileException
import org.apache.tika.Tika

/**
 * Enum class with enumeration of recognizable file formats of service and functions to detect file type
 * @param tika file format name which is returned by [Tika]
 */
enum class Extension(private val tika: String) {
    XLSX("application/x-tika-ooxml"),
    XML("application/xml");

    companion object {
        private val map = Extension.values().associateBy(Extension::tika)
        private val tikaInstance = Tika()

        operator fun get(value: String) = map[value]

        /**
         * Validates file size and file format
         * @param file file as [ByteArray] which needs to be validated
         * @param expected expected extension
         * @return validated file if all checks was passed
         * @throws IncorrectFileException if file was empty or file format was not expected
         */
        fun validate(file: ByteArray, expected: Extension): ByteArray {
            if (file.isEmpty()) throw IncorrectFileException("File was empty")
            val ext = tikaInstance.detect(file)
            if (Extension[ext] != expected) {
                throw IncorrectFileException("Incorrect file format: expected - ${expected.tika}, actual - $ext")
            }
            return file
        }
    }
}

/**
 * Validates file size and file format
 * @receiver file as [ByteArray] which needs to be validated
 * @param expected expected extension
 * @return validated file if all checks was passed
 * @throws IncorrectFileException if file was empty or file format was not expected
 */
infix fun ByteArray.with(expected: Extension) = Extension.validate(this, expected)
