package com.maeasoftworks.alfaconverter.core.datatypes.xsd

open class XType(val name: String) {
	val fields = mutableMapOf<String, XType>()
}