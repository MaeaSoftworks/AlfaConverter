package com.maeasoftworks.alfaconverter.model.datatypes

import org.xlsx4j.sml.Cell

class XNull : XObject() {
	override fun getXlsxRepresentation(): Cell {
		return Cell().also {
			it.t = null
			it.v = null
		}
	}

	override fun getJsonRepresentation(): String {
		return "null"
	}

	override fun getXmlRepresentation(): Any? {
		return null
	}

	override fun getString(): String {
		return "null"
	}

	override fun equals(other: Any?): Boolean {
		return other != null && other is XNull
	}

	override fun hashCode(): Int {
		return 0
	}
}