package com.maeasoftworks.alfaconverter.utils

import com.maeasoftworks.alfaconverter.exceptions.InvalidPartDataTypeException
import com.maeasoftworks.alfaconverter.exceptions.RequiredPartNotFoundException
import com.maeasoftworks.alfaconverter.plugins.serializer
import io.ktor.http.content.*
import kotlinx.serialization.decodeFromString


suspend fun MultiPartData.extractParts(vararg parts: String): NotNullableMap<String, PartData> {
	val result = mutableMapOf<String, PartData>()
	this.forEachPart { part ->
		if (part.name in parts) {
			result[part.name!!] = part
		} else {
			throw RequiredPartNotFoundException(parts.joinToString(", "), result.keys.joinToString(", "))
		}
	}
	if (parts.toList() != result.keys.toList()) {
		throw RequiredPartNotFoundException(parts.joinToString(", "), result.keys.joinToString(", "))
	}
	return NotNullableMap(result)
}

fun PartData.asByteArray(): ByteArray {
	return (this as? PartData.FileItem ?: throw InvalidPartDataTypeException(
		this,
		PartData.FileItem::class
	)).streamProvider().readBytes()
}

inline fun <reified T> PartData.deserializeTo(): T {
	return (this as PartData.FormItem).value.let { serializer.decodeFromString(it) }
}