package com.maeasoftworks.alfaconverter.core.datatypes.xsd

enum class XPrimitive(val xType: () -> XType) {
	ANY_URI         ({ XType("anyURI") }),
	BASE_64_BINARY  ({ XType("base64Binary") }),
	BOOLEAN         ({ XType("boolean") }),
	DATE            ({ XType("date") }),
	DATETIME        ({ XType("dateTime") }),
	DECIMAL         ({ XType("decimal") }),
	DOUBLE          ({ XType("double") }),
	DURATION        ({ XType("duration") }),
	FLOAT           ({ XType("float") }),
	HEX_BINARY      ({ XType("hexBinary") }),
	G_DAY           ({ XType("gDay") }),
	G_MONTH         ({ XType("gMonth") }),
	G_MONTH_DAY     ({ XType("gMonthDay") }),
	G_YEAR          ({ XType("gYear") }),
	G_YEAR_MONTH    ({ XType("gYearMonth") }),
	NOTATION        ({ XType("NOTATION") }),
	QNAME           ({ XType("QName") }),
	STRING          ({ XType("string") }),
	TIME            ({ XType("time") });

	companion object {
		fun findPrimitive(name: String): XPrimitive? {
			return XPrimitive.values().firstOrNull { it.name.replace("_", "").lowercase() == name.split(':').last().lowercase() }
		}
	}
}