package com.maeasoftworks.alfaconverter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.maeasoftworks.alfaconverter.core.datatypes.xsd.XType

class XmlHeadersResponse {
	lateinit var headers: List<String?>
	@get:JsonProperty("first-line")
	lateinit var firstLine: List<String?>
	lateinit var schema: List<XType>
}