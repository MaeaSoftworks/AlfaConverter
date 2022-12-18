package com.maeasoftworks.alfaconverter.core.xml.structure

import javax.xml.crypto.Data

object Constraints {
	interface Length {
		var length: Int?
	}

	interface MinLength {
		var minLength: Int?
	}

	interface MaxLength {
		var maxLength: Int?
	}

	interface Pattern {
		var pattern: String?
	}

	interface Enumeration {
		var values: MutableList<Data>?
	}

	interface WhiteSpace {
		var whiteSpace: String?
	}

	interface MaxInclusive {
		var maxInclusive: Int?
	}

	interface MaxExclusive {
		var maxExclusive: Int?
	}

	interface MinExclusive {
		var minExclusive: Int?
	}

	interface MinInclusive {
		var minInclusive: Int?
	}

	interface TotalDigits {
		var totalDigits: Int?
	}

	interface FractionDigits {
		var fractionDigits: Int?
	}
}