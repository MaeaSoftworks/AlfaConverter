package com.maeasoftworks.alfaconverter.exceptions

import io.ktor.http.content.*
import kotlin.reflect.KClass

class IncorrectFileException(message: String) : Exception(message)

class InvalidPartDataTypeException(partData: PartData, expectedType: KClass<*>) :
	Exception("Part '${partData.name}' was in incorrect type. Expected: ${expectedType.simpleName}, actual: ${partData::class.simpleName}")

class RequiredPartNotFoundException(expected: String, actual: String) : Exception("Inconsistent form-data parts: expected: [$expected], actual: [$actual]")

class UnprocessableSchemaException(message: String) : Exception("Provided xml file is not valid xsd schema: $message")