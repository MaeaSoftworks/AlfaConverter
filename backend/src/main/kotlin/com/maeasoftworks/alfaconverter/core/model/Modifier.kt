package com.maeasoftworks.alfaconverter.core.model

interface Modifier {
	fun getHeaders(): List<ColumnAddress>
	fun getAdditionalData(): Any?
}