package com.maeasoftworks.alfaconverter.core.model

interface Result {
	var table: Table

	fun initialize(modifier: Modifier?)

	fun convert(): ByteArray
}