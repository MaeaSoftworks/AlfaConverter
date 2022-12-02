package com.maeasoftworks.alfaconverter.core.datatypes.xsd

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("Type")
sealed class Type {
	abstract val name: String

	@Transient
	var dependent = 0
}