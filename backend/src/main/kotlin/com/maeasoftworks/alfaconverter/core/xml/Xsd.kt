package com.maeasoftworks.alfaconverter.core.xml

import com.maeasoftworks.alfaconverter.core.model.Modifier

class Xsd(xsd: ByteArray) : Modifier {
	private var schema = Schema(String(xsd))

	override fun getHeaders() = schema.convertElementsToHeaders()

	override fun getAdditionalData() = schema.elements.first { it.type.dependent == 0 }
}