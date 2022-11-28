package com.maeasoftworks.alfaconverter.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Lines(
	val headers: List<String?>,
	@get:JsonProperty("first-line")
	val firstLine: List<String?>
)