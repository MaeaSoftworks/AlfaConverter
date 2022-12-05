package com.maeasoftworks.alfaconverter.core.conversions

import kotlinx.serialization.Serializable

@Serializable
class Path {
	var position: Int = -1
	var path: String = ""
	val type: Type

	constructor(position: Int) {
		this.position = position
		this.type = Type.POSITIONAL
	}

	constructor(path: String) {
		this.path = path
		this.type = Type.NAMED
	}

	constructor(vararg path: String) {
		this.path = path.joinToString(".")
		this.type = Type.NAMED
	}

	constructor(path: List<String>) {
		this.path = path.joinToString(".")
		this.type = Type.NAMED
	}

	override fun hashCode(): Int {
		return if (type == Type.POSITIONAL)
			position * 17
		else
			path.hashCode() * 29
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Path

		if (type != other.type) return false
		if (type == Type.POSITIONAL) {
			if (position != other.position) return false
		} else {
			if (path != other.path) return false
		}

		return true
	}

	override fun toString(): String {
		return if (type == Type.POSITIONAL) position.toString() else path
	}

	enum class Type {
		POSITIONAL,
		NAMED
	}
}