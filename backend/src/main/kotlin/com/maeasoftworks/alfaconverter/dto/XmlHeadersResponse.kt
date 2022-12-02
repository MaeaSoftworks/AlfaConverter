package com.maeasoftworks.alfaconverter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

class XmlHeadersResponse {
	lateinit var headers: List<String?>
	@get:JsonProperty("first-line")
	lateinit var firstLine: List<String?>
	lateinit var schema: JsonNode
}