package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.Serializable

object Primitive {
	@Serializable object AnyUri : Type("anyURI")
	@Serializable object Base64Binary : Type("base64Binary")
	@Serializable object Boolean : Type("boolean")
	@Serializable object Date : Type("date")
	@Serializable object Datetime : Type("dateTime")
	@Serializable object Decimal : Type("decimal")
	@Serializable object Double : Type("double")
	@Serializable object Duration : Type("duration")
	@Serializable object Float : Type("float")
	@Serializable object HexBinary : Type("hexBinary")
	@Serializable object GDay : Type("gDay")
	@Serializable object GMonth : Type("gMonth")
	@Serializable object GMonthDay : Type("gMonthDay")
	@Serializable object GYear : Type("gYear")
	@Serializable object GYearMonth : Type("gYearMonth")
	@Serializable object Notation : Type("NOTATION")
	@Serializable object QName : Type("QName")
	@Serializable object String : Type("string")
	@Serializable object Time : Type("time")

	private val subclasses = listOf(
		AnyUri,
		Base64Binary,
		Boolean,
		Date,
		Datetime,
		Decimal,
		Double,
		Duration,
		Float,
		HexBinary,
		GDay,
		GMonth,
		GMonthDay,
		GYear,
		GYearMonth,
		Notation,
		QName,
		String,
		Time
	)

	fun findPrimitive(name: kotlin.String, prefix: kotlin.String): Type? {
		return name.removePrefix("$prefix:").let { n -> subclasses.firstOrNull { it.name == n } }
	}
}