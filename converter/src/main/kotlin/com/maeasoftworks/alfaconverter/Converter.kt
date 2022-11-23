package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.model.BondedPair
import com.maeasoftworks.alfaconverter.documents.Document
import com.maeasoftworks.alfaconverter.documents.XlsxDocument
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Converter private constructor() {
	internal val documents: BondedPair<Document> = BondedPair()
	internal var conversion: Conversion = Conversion.empty

	private fun addFile(file: ByteArray, extension: String) {
		documents += when(extension) {
			"xlsx" -> XlsxDocument().open(file)
			else -> throw Exception("Incorrect file extension")
		}
	}

	fun initialize(): Converter {
		documents.map { it.table = it.createTable() }
		conversion.register(documents)
		return this
	}

	fun setHeadship(headship: Int) {
		documents.dependence = BondedPair.Headship.values()[headship]
	}

	fun getHeaders(): List<List<String?>> {
		return listOf(documents.first!!.getHeaders(), documents.second!!.getHeaders())
	}

	fun setConversion(conversion: String) = setConversion(Json.decodeFromString(conversion) as Conversion)

	internal fun setConversion(conversion: Conversion) : Converter {
		this.conversion = conversion
		return this
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