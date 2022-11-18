package com.maeasoftworks.alfaconverter.model.datatypes

import org.xlsx4j.sml.Cell

abstract class XObject {
	abstract fun getXlsxRepresentation(): Cell

	abstract fun getJsonRepresentation(): String

	abstract fun getXmlRepresentation(): Any?

	abstract fun getString(): String

	abstract override fun equals(other: Any?): Boolean

	abstract override fun hashCode(): Int
}