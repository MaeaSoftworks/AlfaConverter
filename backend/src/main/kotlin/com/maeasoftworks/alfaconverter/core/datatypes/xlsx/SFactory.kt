package com.maeasoftworks.alfaconverter.core.datatypes.xlsx

import com.maeasoftworks.alfaconverter.core.conversions.TypeConversion
import java.io.InvalidClassException

object SFactory {
	fun create(conversion: TypeConversion, value: SObject): SObject {
		return when (conversion.targetType) {
			STypeName.SString -> SString(value.getString())
			STypeName.SBoolean -> SBoolean(value.getString() == "true")
			STypeName.SNumber -> SNumber(value.getString().toDouble(), conversion.dataFormat!!)
			STypeName.SNull -> SNull()
			STypeName.SObject -> throw InvalidClassException("${STypeName.SObject} is abstract class")
		}
	}
}