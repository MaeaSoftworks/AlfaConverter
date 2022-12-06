package com.maeasoftworks.alfaconverter.core.datatypes.xlsx

import org.xlsx4j.sml.Cell

abstract class SObject {
	abstract fun getXlsxRepresentation(): Cell

	abstract fun getJsonRepresentation(): String

	abstract fun getXmlRepresentation(): String

	abstract fun getString(): String

	abstract override fun equals(other: Any?): Boolean

	abstract override fun hashCode(): Int
}