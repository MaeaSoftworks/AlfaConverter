package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.annotations.UsedExternally
import com.maeasoftworks.alfaconverter.exceptions.InvalidOperationException
import com.maeasoftworks.alfaconverter.model.BondedPair

class ConverterContainer {
	internal lateinit var converter: Converter

	@UsedExternally
	fun initialize(file1: ByteArray, file2: ByteArray) {
		if (this::converter.isInitialized) {
			throw InvalidOperationException("Converter was already initialized")
		} else {
			converter = Converter.ofFiles(file1, file2)
			converter.initialize()
		}
	}

	@UsedExternally
	fun setHeadship(headship: BondedPair.Headship) = converter.setHeadship(headship)

	@UsedExternally
	fun setHeadship(dependent: Int) = setHeadship(BondedPair.Headship.values()[dependent])

	@UsedExternally
	fun getHeaders(file1: ByteArray, file2: ByteArray): List<List<String?>> {
		return listOf(file1, file2).map { Converter.ofFile(it).getHeaders() }
	}

	@UsedExternally
	fun setConversion(conversion: String) = converter.setConversion(conversion)

	@UsedExternally
	fun convert() = converter.convert()

	internal fun setConversion(conversion: Conversion) = converter.setConversion(conversion)
}