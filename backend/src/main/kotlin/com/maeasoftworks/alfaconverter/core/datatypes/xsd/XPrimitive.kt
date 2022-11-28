package com.maeasoftworks.alfaconverter.core.datatypes.xsd

enum class XPrimitive(val xType: () -> XType) {
	ANY_URI         ({ XType("anyURI"      , Complexity.PRIMITIVE) }),
	BASE_64_BINARY  ({ XType("base64Binary", Complexity.PRIMITIVE) }),
	BOOLEAN         ({ XType("boolean"     , Complexity.PRIMITIVE) }),
	DATE            ({ XType("date"        , Complexity.PRIMITIVE) }),
	DATETIME        ({ XType("dateTime"    , Complexity.PRIMITIVE) }),
	DECIMAL         ({ XType("decimal"     , Complexity.PRIMITIVE) }),
	DOUBLE          ({ XType("double"      , Complexity.PRIMITIVE) }),
	DURATION        ({ XType("duration"    , Complexity.PRIMITIVE) }),
	FLOAT           ({ XType("float"       , Complexity.PRIMITIVE) }),
	HEX_BINARY      ({ XType("hexBinary"   , Complexity.PRIMITIVE) }),
	G_DAY           ({ XType("gDay"        , Complexity.PRIMITIVE) }),
	G_MONTH         ({ XType("gMonth"      , Complexity.PRIMITIVE) }),
	G_MONTH_DAY     ({ XType("gMonthDay"   , Complexity.PRIMITIVE) }),
	G_YEAR          ({ XType("gYear"       , Complexity.PRIMITIVE) }),
	G_YEAR_MONTH    ({ XType("gYearMonth"  , Complexity.PRIMITIVE) }),
	NOTATION        ({ XType("NOTATION"    , Complexity.PRIMITIVE) }),
	QNAME           ({ XType("QName"       , Complexity.PRIMITIVE) }),
	STRING          ({ XType("string"      , Complexity.PRIMITIVE) }),
	TIME            ({ XType("time"        , Complexity.PRIMITIVE) });

	companion object {
		fun findPrimitive(name: String): XPrimitive? {
			return XPrimitive.values().firstOrNull { it.name.replace("_", "").lowercase() == name.split(':').last().lowercase() }
		}
	}
}