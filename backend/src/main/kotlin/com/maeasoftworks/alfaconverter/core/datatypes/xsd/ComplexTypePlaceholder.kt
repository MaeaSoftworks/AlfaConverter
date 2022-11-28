package com.maeasoftworks.alfaconverter.core.datatypes.xsd

class ComplexTypePlaceholder(
	val typeName: String,
	val fieldName: String,
	val instance: XType
) : XType("${instance}\$${fieldName}")