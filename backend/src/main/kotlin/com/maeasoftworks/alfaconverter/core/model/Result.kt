package com.maeasoftworks.alfaconverter.core.model

import com.maeasoftworks.alfaconverter.core.Converter

interface Result {
	var table: Table

	fun initialize(parent: Converter<*, *, *>)

	fun convert(): ByteArray
}