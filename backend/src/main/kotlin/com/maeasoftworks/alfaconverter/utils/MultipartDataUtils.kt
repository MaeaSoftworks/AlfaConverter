package com.maeasoftworks.alfaconverter.utils

import com.maeasoftworks.alfaconverter.exceptions.InvalidPartDataTypeException
import com.maeasoftworks.alfaconverter.exceptions.RequiredPartNotFoundException
import com.maeasoftworks.alfaconverter.plugins.serializer
import io.ktor.http.content.*
import kotlinx.serialization.decodeFromString

suspend fun MultiPartData.extractParts(vararg parts: String): List<PartData> {
	val result = mutableMapOf<String, PartData>()
	this.forEachPart { part ->
		if (part.name in parts) {
			result[part.name!!] = part
		} else {
			throw RequiredPartNotFoundException(parts.joinToString(", "), result.keys.joinToString(", "))
		}
	}
	if (!parts.toList().equalsIgnoreOrder(result.keys.toList())) {
		throw RequiredPartNotFoundException(parts.joinToString(", "), result.keys.joinToString(", "))
	}
	return parts.map { result[it]!! }.toList()
}

suspend inline fun <T1, T2> MultiPartData.extractParts(
	part1: Pair<String, (PartData) -> T1>,
	part2: Pair<String, (PartData) -> T2>
): Pair<T1, T2> = extractParts(part1.first, part2.first).let { part1.second(it[0]) to part2.second(it[1]) }

suspend inline fun <T1, T2, T3> MultiPartData.extractParts(
	part1: Pair<String, (PartData) -> T1>,
	part2: Pair<String, (PartData) -> T2>,
	part3: Pair<String, (PartData) -> T3>,
): Triple<T1, T2, T3> = extractParts(part1.first, part2.first, part3.first).let {
	Triple(part1.second(it[0]), part2.second(it[1]), part3.second(it[2]))
}

fun PartData.asByteArray(): ByteArray {
	return (this as? PartData.FileItem ?: throw InvalidPartDataTypeException(
		this,
		PartData.FileItem::class
	)).streamProvider().readBytes()
}

inline fun <reified T> PartData.deserializeTo(): T {
	return serializer.decodeFromString((this as PartData.FormItem).value)
}