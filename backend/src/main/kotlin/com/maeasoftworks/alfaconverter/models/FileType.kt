package com.maeasoftworks.alfaconverter.models

enum class FileType(val tikaExtension: String) {
	XLSX("application/x-tika-ooxml"),
	XML("application/xml");

	companion object {
		private val map = FileType.values().associateBy(FileType::tikaExtension)
		operator fun get(value: String) = map[value]
	}
}