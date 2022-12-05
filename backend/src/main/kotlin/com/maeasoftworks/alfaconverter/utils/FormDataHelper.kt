package com.maeasoftworks.alfaconverter.utils

import io.ktor.http.content.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


suspend fun MultiPartData.extractParts(vararg parts: String): Map<String, PartData?> {
	val result = mutableMapOf<String, PartData?>()
	this.forEachPart { part ->
		if (part.name in parts) {
			result[part.name!!] = part
		} else {
			result[part.name!!] = null
		}
	}
	return result
}

fun PartData?.tryGetBytes(): ByteArray {
	return (this as? PartData.FileItem ?: throw IllegalArgumentException()).streamProvider().readBytes()
}

inline fun <reified T> PartData?.deserialize(): T? {
	return (this as? PartData.FormItem)?.value?.let { Json.decodeFromString(it) }
}