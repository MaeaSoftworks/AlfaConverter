package com.maeasoftworks.alfaconverter.dto

import kotlinx.serialization.SerialName

data class Lines(
	val headers: List<String?>,
	@SerialName("first-line")
	val firstLine: List<String?>
)