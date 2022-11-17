package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.conversions.Conversion
import com.maeasoftworks.alfaconverter.exceptions.UnsupportedExtensionException
import com.maeasoftworks.alfaconverter.model.BondedPair
import com.maeasoftworks.alfaconverter.model.documents.Document
import com.maeasoftworks.alfaconverter.model.documents.XlsxDocument
import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Converter internal constructor() {
	internal val documents: BondedPair<Document> = BondedPair()
	internal var conversion: Conversion = Conversion.empty

	private fun addFile(file: ByteArray, extension: String) {
		documents += when (extension) {
			"xlsx" -> XlsxDocument().open(file)
			else -> throw UnsupportedExtensionException(extension)
		}
	}

	fun initialize(): Converter {
		documents.forEach { it.initializeTable() }
		conversion.register(documents)
		return this
	}

	fun setHeadship(headship: Int) {
		documents.headship = BondedPair.Headship.values()[headship]
	}

	fun getHeaders(): List<List<String?>> {
		return documents.first!!.getHeadersAndExamples() + documents.second!!.getHeadersAndExamples()
	}

	internal fun setConversion(conversion: Conversion): Converter {
		this.conversion = conversion
		return this
	}

	fun setConversion(conversion: String): Converter {
		return setConversion(Json.decodeFromString(conversion) as Conversion)
	}

	fun convert(): ByteArray {
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

		internal fun ofTables(table1: Table, table2: Table): Converter {
			return Converter().also {
				it.documents.first = object : Document() {
					override fun open(file: ByteArray) = this
					override fun save() = ByteArray(0)
					override fun initializeTable() { table = table1 }
					override fun getHeadersAndExamples(): List<List<String?>> = listOf()
				}

				it.documents.second = object : Document() {
					override fun open(file: ByteArray) = this
					override fun save() = ByteArray(0)
					override fun initializeTable() { table = table2 }
					override fun getHeadersAndExamples(): List<List<String?>> = listOf()
				}
			}
		}
	}
}