package com.maeasoftworks.alfaconverter.core.xlsx.structure

import java.io.InvalidClassException

object Factory {
	fun create(value: Data, targetType: TypeName, dataFormat: Long?): Data {
		return when (targetType) {
			TypeName.String -> StringData(value.getString())
			TypeName.Boolean -> BooleanData(value.getString() == "true")
			TypeName.Number -> NumberData(value.getString().toDouble(), dataFormat!!)
			TypeName.Null -> NullData()
			TypeName.Object -> throw InvalidClassException("${TypeName.Object} is abstract class")
		}
	}
}