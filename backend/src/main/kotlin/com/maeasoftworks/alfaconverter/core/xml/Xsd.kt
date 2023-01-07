package com.maeasoftworks.alfaconverter.core.xml

import com.maeasoftworks.alfaconverter.core.Modifier

class Xsd(xsd: ByteArray) : Modifier {
    private var schema = Schema(String(xsd))

    override fun getHeaders() = schema.extractElementHeaders()

    override fun getPayload() = schema.rootElement
}
