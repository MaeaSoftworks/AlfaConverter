package com.maeasoftworks.alfaconverter.core.conversions

import kotlinx.serialization.Serializable

@Serializable
data class Target(var type: Type) {
	var positionalTarget = -1
	var typeName = ""
	var fieldName = ""

	constructor(positionalTarget: Int) : this(Type.POSITIONAL) {
		this.positionalTarget = positionalTarget
	}

	constructor(typeName: String, fieldName: String) : this(Type.NAMED) {
		this.typeName = typeName
		this.fieldName = fieldName
	}

	override fun hashCode(): Int {
		return if (type == Type.POSITIONAL)
			positionalTarget * 17
		else
			(typeName.hashCode() * 19 + fieldName.hashCode() * 23) * 29
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Target

		if (type != other.type) return false
		if (type == Type.POSITIONAL) {
			if (positionalTarget != other.positionalTarget) return false
		} else {
			if (typeName != other.typeName) return false
			if (fieldName != other.fieldName) return false
		}

		return true
	}

	override fun toString(): String {
		return if (type == Type.POSITIONAL) positionalTarget.toString() else "${typeName}\$${fieldName}"
	}


	enum class Type {
		POSITIONAL,
		NAMED
	}
}

val Int.pos: Target
	get() {
		return Target(this)
	}

infix fun String.at(typeName: String): Target {
 	return Target(typeName, this)
}