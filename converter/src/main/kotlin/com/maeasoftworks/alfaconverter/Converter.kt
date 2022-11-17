package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.documents.Document
import com.maeasoftworks.alfaconverter.documents.XlsxDocument
import com.maeasoftworks.alfaconverter.exceptions.UnsupportedExtensionException
import com.maeasoftworks.alfaconverter.model.BondedPair
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Converter private constructor() {
	internal val documents: BondedPair<Document> = BondedPair()
	internal var conversion: Conversion = Conversion.empty

	private fun addFile(file: ByteArray, extension: String) {
		documents += when (extension) {
			"xlsx" -> XlsxDocument().open(file)
			else -> throw UnsupportedExtensionException(extension)
		}
	}

	fun initialize(): Converter {
		documents.map { it.table = it.createTable() }
		conversion.register(documents)
		return this
	}

	fun setHeadship(headship: Int) {
		documents.headship = BondedPair.Headship.values()[headship]
	}

	fun getHeaders(): List<List<String?>> {
		return listOf(documents.first!!.getHeaders(), documents.second!!.getHeaders())
	}

	internal fun setConversion(conversion: Conversion): Converter {
		this.conversion = conversion
		return this
	}

	fun setConversion(conversion: String) : Converter {
		return setConversion(Json.decodeFromString(conversion) as Conversion)
	}

	fun convert(): ByteArray {
		initialize()
		conversion.start()
		return documents.slave.save()
	}

	companion object {
		fun ofFiles(file1: ByteArray, file2: ByteArray, extension: String): Converter {
			return Converter().also {
				it.addFile(file1, extension)
				it.addFile(file2, extension)
			}
		}
	}
}