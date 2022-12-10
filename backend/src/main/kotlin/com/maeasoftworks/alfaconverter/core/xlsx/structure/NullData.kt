package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.xlsx4j.sml.Cell

class NullData : Data() {
	override fun getXlsxRepresentation(): Cell {
		return Cell().also {
			it.t = null
			it.v = null
		}
	}

	override fun getJsonRepresentation(): String {
		return "null"
	}

	override fun getXmlRepresentation(): String {
		return "null"
	}

	override fun getString(): String {
		return "null"
	}

	override fun equals(other: Any?): Boolean {
		return other != null && other is NullData
	}

	override fun hashCode(): Int {
		return 0
	}
}