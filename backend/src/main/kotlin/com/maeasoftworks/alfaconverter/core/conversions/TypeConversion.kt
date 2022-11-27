package com.maeasoftworks.alfaconverter.core.conversions

import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.STypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeConversion(
	@SerialName("target-type")
	val targetType: STypeName,
	@SerialName("data-format")
	val dataFormat: Long?
)
