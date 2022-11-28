package com.maeasoftworks.alfaconverter.core.datatypes.xsd

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.serialization.Serializable

@Serializable
open class XType(
	val name: String,
	var complexity: Complexity = Complexity.COMPLEX_TYPE
) {
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	val fields = mutableMapOf<String, XType>()

	/**
	 * Count of XType instances which depends on this XType.
	 */
	@JsonIgnore
	var dependent = 0
}