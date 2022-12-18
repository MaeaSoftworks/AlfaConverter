package com.maeasoftworks.alfaconverter.core.xml.structure

object DerivedTypes {
	open class NormalizedString: Primitive.String() {
		init {
			name = "normalizedString"
		}
	}

	open class Token: NormalizedString() {
		init {
			name = "token"
		}
	}

	open class Language: Token() {
		init {
			name = "language"
		}
	}

	open class NMToken: Token() {
		init {
			name = "NMTOKEN"
		}
	}

	open class NMTokens: NMToken() {
		init {
			name = "NMTOKENS"
		}
	}

	open class Name: Token() {
		init {
			name = "Name"
		}
	}

	open class NCName: Name() {
		init {
			name = "NCName"
		}
	}

	open class ID: NCName() {
		init {
			name = "ID"
		}
	}

	open class IDRef: ID() {
		init {
			name = "IDREF"
		}
	}

	open class IDRefs: IDRef() {
		init {
			name = "IDREFS"
		}
	}

	open class Entity: NCName() {
		init {
			name = "ENTITY"
		}
	}

	open class Entities: Entity() {
		init {
			name = "ENTITIES"
		}
	}

	open class Integer: Primitive.Decimal() {
		init {
			name = "integer"
		}
	}

	open class NonPositiveInteger: Integer() {
		init {
			name = "nonPositiveInteger"
		}
	}

	open class NegativeInteger: Integer() {
		init {
			name = "negativeInteger"
		}
	}

	open class Long: Integer() {
		init {
			name = "long"
		}
	}

	open class Int: Long() {
		init {
			name = "int"
		}
	}

	open class Short: Int() {
		init {
			name = "short"
		}
	}

	open class Byte: Short() {
		init {
			name = "byte"
		}
	}

	open class NonNegativeInteger: Integer() {
		init {
			name = "nonNegativeInteger"
		}
	}

	open class UnsignedLong: NonNegativeInteger() {
		init {
			name = "unsignedLong"
		}
	}

	open class UnsignedInt: UnsignedLong() {
		init {
			name = "unsignedInt"
		}
	}

	open class UnsignedShort: UnsignedInt() {
		init {
			name = "unsignedShort"
		}
	}

	open class UnsignedByte: UnsignedShort() {
		init {
			name = "unsignedByte"
		}
	}

	open class PositiveInteger: NonNegativeInteger() {
		init {
			name = "positiveInteger"
		}
	}
}