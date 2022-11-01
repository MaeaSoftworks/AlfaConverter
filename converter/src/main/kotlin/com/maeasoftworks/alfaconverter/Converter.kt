package com.maeasoftworks.alfaconverter

class Converter(private val firstFile: ByteArray, private val secondFile: ByteArray) {
	fun convert() : ByteArray {
		return if (firstFile.isNotEmpty() && secondFile.isNotEmpty()) ByteArray(1) else ByteArray(0)
	}
}