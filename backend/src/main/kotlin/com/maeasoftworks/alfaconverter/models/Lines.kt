package com.maeasoftworks.alfaconverter.models

import kotlinx.serialization.Serializable

@Serializable
data class Lines(
	val headers: List<String?>,
	val firstLine: List<String?>
)