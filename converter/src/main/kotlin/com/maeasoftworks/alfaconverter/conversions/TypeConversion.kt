package com.maeasoftworks.alfaconverter.conversions

import com.maeasoftworks.alfaconverter.model.datatypes.XTypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeConversion(
	@SerialName("target-type")
	val targetType: XTypeName,
	@SerialName("data-format")
	val dataFormat: Long?
)
