package com.maeasoftworks.alfaconverter.core.xlsx.structure

import java.io.InvalidClassException

object SFactory {
	fun create(value: SObject, targetType: STypeName, dataFormat: Long?): SObject {
		return when (targetType) {
			STypeName.SString -> SString(value.getString())
			STypeName.SBoolean -> SBoolean(value.getString() == "true")
			STypeName.SNumber -> SNumber(value.getString().toDouble(), dataFormat!!)
			STypeName.SNull -> SNull()
			STypeName.SObject -> throw InvalidClassException("${STypeName.SObject} is abstract class")
		}
	}
}