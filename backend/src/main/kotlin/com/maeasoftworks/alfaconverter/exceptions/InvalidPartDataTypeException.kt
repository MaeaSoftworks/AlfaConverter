package com.maeasoftworks.alfaconverter.exceptions

import io.ktor.http.content.*
import kotlin.reflect.KClass

class InvalidPartDataTypeException(partData: PartData, expectedType: KClass<*>) :
	Exception("Part '${partData.name}' was in incorrect type. Expected: ${expectedType.simpleName}, actual: ${partData::class.simpleName}")