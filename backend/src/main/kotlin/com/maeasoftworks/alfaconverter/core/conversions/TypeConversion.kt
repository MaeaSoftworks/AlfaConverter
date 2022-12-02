package com.maeasoftworks.alfaconverter.core.conversions

import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.STypeName
import kotlinx.serialization.Serializable

@Serializable
data class TypeConversion(
	val targetType: STypeName,
	val dataFormat: Long?
)
