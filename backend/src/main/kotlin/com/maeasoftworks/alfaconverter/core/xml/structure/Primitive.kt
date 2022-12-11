package com.maeasoftworks.alfaconverter.core.xml.structure

enum class Primitive(val xsdName: String, val element: () -> SimpleType) {
	ANY_URI("anyURI", { SimpleType("anyURI") }),
	BASE_64_BINARY("base64Binary", { SimpleType("base64Binary") }),
	BOOLEAN("boolean", { SimpleType("boolean") }),
	DATE("date", { SimpleType("date") }),
	DATETIME("dateTime", { SimpleType("dateTime") }),
	DECIMAL("decimal", { SimpleType("decimal") }),
	DOUBLE("double", { SimpleType("double") }),
	DURATION("duration", { SimpleType("duration") }),
	FLOAT("float", { SimpleType("float") }),
	HEX_BINARY("hexBinary", { SimpleType("hexBinary") }),
	G_DAY("gDay", { SimpleType("gDay") }),
	G_MONTH("gMonth", { SimpleType("gMonth") }),
	G_MONTH_DAY("gMonthDay", { SimpleType("gMonthDay") }),
	G_YEAR("gYear", { SimpleType("gYear") }),
	G_YEAR_MONTH("gYearMonth", { SimpleType("gYearMonth") }),
	NOTATION("NOTATION", { SimpleType("NOTATION") }),
	QNAME("QName", { SimpleType("QName") }),
	STRING("string", { SimpleType("string") }),
	TIME("time", { SimpleType("time") });

	companion object {
		fun findPrimitive(name: String, xsdPrefix: String): Primitive? {
			return name.removePrefix("$xsdPrefix:").let { n -> Primitive.values().firstOrNull { it.xsdName == n } }
		}
	}
}

operator fun Primitive.invoke(): SimpleType {
	return this.element()
}