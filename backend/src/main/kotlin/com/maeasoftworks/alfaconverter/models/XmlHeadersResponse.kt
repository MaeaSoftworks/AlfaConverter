package com.maeasoftworks.alfaconverter.models

import com.maeasoftworks.alfaconverter.core.datatypes.xsd.Element
import kotlinx.serialization.Serializable

@Serializable
class XmlHeadersResponse {
	lateinit var headers: List<String?>
	lateinit var firstLine: List<String?>
	lateinit var schema: Element
}