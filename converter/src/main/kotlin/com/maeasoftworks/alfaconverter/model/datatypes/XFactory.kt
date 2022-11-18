package com.maeasoftworks.alfaconverter.model.datatypes

import com.maeasoftworks.alfaconverter.conversions.TypeConversion
import java.io.InvalidClassException

object XFactory {
	fun create(conversion: TypeConversion, value: XObject): XObject {
		return when (conversion.targetType) {
			XTypeName.XString -> XString(value.getString())
			XTypeName.XBoolean -> XBoolean(value.getString() == "true")
			XTypeName.XNumber -> XNumber(value.getString().toDouble(), conversion.dataFormat!!)
			XTypeName.XNull -> XNull()
			XTypeName.XObject -> throw InvalidClassException("${XTypeName.XObject} is abstract class")
		}
	}
}