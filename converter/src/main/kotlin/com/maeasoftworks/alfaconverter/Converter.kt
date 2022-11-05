package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.exceptions.InvalidOperationException
import com.maeasoftworks.alfaconverter.model.BondedPair
import com.maeasoftworks.alfaconverter.wrappers.Document
import com.maeasoftworks.alfaconverter.wrappers.Table
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart
import java.io.ByteArrayInputStream

internal class Converter {
	private val isLiteMode: Boolean
		get() = documents.size == 1

	internal val documents: BondedPair<Document> = BondedPair()
	internal lateinit var conversion: Conversion
		private set

	private fun addFile(file: ByteArray) {
		documents += Document().also {
			it.document = SpreadsheetMLPackage.load(ByteArrayInputStream(file))
			it.worksheet = (it.document.parts[worksheet] as WorksheetPart).jaxbElement
		}
	}

	internal fun initialize(): Converter {
		if (isLiteMode) throw InvalidOperationException("Converter was in lite mode")
		documents.map { it.table = Table.create(it.document, it.worksheet) }
		return this
	}

	internal fun setHeadship(headship: BondedPair.Headship) {
		documents.dependence = headship
	}

	internal fun getHeaders(): List<String?> {
		if (!isLiteMode) throw InvalidOperationException("Converter was not in lite mode")
		return Table.getHeaders(documents.first!!.document, documents.first!!.worksheet)
	}

	internal fun setConversion(conversion: String) = setConversion(Json.decodeFromString(conversion) as Conversion)

	internal fun setConversion(conversion: Conversion) {
		if (isLiteMode) throw InvalidOperationException("Converter was in lite mode")
		this.conversion = conversion
		if (!this::conversion.isInitialized) throw InvalidOperationException("Conversion was not initialized")
		conversion.register(documents)
	}

	internal fun convert(): ByteArray {
		initialize()
		conversion.start()
		return documents.slave.table.save()
	}

	companion object {
		private val worksheet = PartName("/xl/worksheets/sheet1.xml")

		internal fun ofFile(file: ByteArray): Converter {
			return Converter().also { it.addFile(file) }
		}

		internal fun ofFiles(file1: ByteArray, file2: ByteArray): Converter {
			return Converter().also {
				it.addFile(file1)
				it.addFile(file2)
			}
		}
	}
}