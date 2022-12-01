package com.maeasoftworks.alfaconverter.core.datatypes.xsd

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude

open class XType(
	val typename: String,
	@Suppress("unused")
	var complexity: Complexity = Complexity.COMPLEX_TYPE
) {
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	val fields = mutableMapOf<String, XType>()

	/**
	 * Count of XType instances which depends on this XType.
	 */
	@JsonIgnore
	var dependent = 0

	@JsonIgnore
	var dsl = DSL()

	constructor(
		typename: String,
		complexity: Complexity = Complexity.COMPLEX_TYPE,
		initialization: DSL.() -> Unit
	) : this(typename, complexity) {
		dsl.initialization()
	}

	inner class DSL {
		infix fun String.of(type: XType) {
			fields[this] = type
		}

		infix fun String.of(type: XPrimitive) {
			fields[this] = type.xType()
		}
	}
}