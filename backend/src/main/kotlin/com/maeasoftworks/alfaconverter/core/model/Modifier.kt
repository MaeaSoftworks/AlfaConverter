package com.maeasoftworks.alfaconverter.core.model

interface Modifier {
	fun getHeaders(): List<String>
	fun getAdditionalData(): Any?
}