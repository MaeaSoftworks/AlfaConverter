package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.xml.crypto.Data
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.Length
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.MinLength
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.MaxLength
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.Pattern
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.Enumeration
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.WhiteSpace
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.MaxInclusive
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.MaxExclusive
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.MinExclusive
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.MinInclusive
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.TotalDigits
import com.maeasoftworks.alfaconverter.core.xml.structure.Constraints.FractionDigits

object Primitive {
	@Serializable
	@SerialName("wrappers\$numeric")
	abstract class Numeric: Type, Pattern, Enumeration, WhiteSpace, MaxInclusive, MaxExclusive, MinInclusive, MinExclusive {
		constructor(name: kotlin.String) : super(name)

		override var pattern: kotlin.String? = null
		override var values: MutableList<Data>? = null
		override var whiteSpace: kotlin.String? = null
		override var maxInclusive: Int? = null
		override var maxExclusive: Int? = null
		override var minExclusive: Int? = null
		override var minInclusive: Int? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$anyURI")
	open class AnyUri : Type("anyURI"), Length, MinLength, MaxLength, Pattern, Enumeration, WhiteSpace {
		override var length: Int? = null
		override var minLength: Int? = null
		override var maxLength: Int? = null
		override var pattern: kotlin.String? = null
		override var values: MutableList<Data>? = null
		override var whiteSpace: kotlin.String? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$base64Binary")
	open class Base64Binary : Type("base64Binary"), Length, MinLength, MaxLength, Pattern, Enumeration, WhiteSpace {
		override var length: Int? = null
		override var minLength: Int? = null
		override var maxLength: Int? = null
		override var pattern: kotlin.String? = null
		override var values: MutableList<Data>? = null
		override var whiteSpace: kotlin.String? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$boolean")
	open class Boolean : Type("boolean"), Pattern, WhiteSpace {
		override var pattern: kotlin.String? = null
		override var whiteSpace: kotlin.String? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$date")
	open class Date : Type("date")

	@Serializable
	@SerialName("xsd\$primitive\$dateTime")
	open class Datetime : Numeric("dateTime")

	@Serializable
	@SerialName("xsd\$primitive\$decimal")
	open class Decimal : Numeric("decimal"), TotalDigits, FractionDigits {
		override var totalDigits: Int? = null
		override var fractionDigits: Int? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$double")
	open class Double : Numeric("double")

	@Serializable
	@SerialName("xsd\$primitive\$duration")
	open class Duration : Numeric("duration")

	@Serializable
	@SerialName("xsd\$primitive\$float")
	open class Float : Numeric("float")

	@Serializable
	@SerialName("xsd\$primitive\$hexBinary")
	open class HexBinary : Type("hexBinary"), Length, MinLength, MaxLength, Pattern, Enumeration, WhiteSpace {
		override var length: Int? = null
		override var minLength: Int? = null
		override var maxLength: Int? = null
		override var pattern: kotlin.String? = null
		override var values: MutableList<Data>? = null
		override var whiteSpace: kotlin.String? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$gDay")
	open class GDay : Numeric("gDay")

	@Serializable
	@SerialName("xsd\$primitive\$gMonth")
	open class GMonth : Numeric("gMonth")

	@Serializable
	@SerialName("xsd\$primitive\$gMonthDay")
	open class GMonthDay : Numeric("gMonthDay")

	@Serializable
	@SerialName("xsd\$primitive\$gYear")
	open class GYear : Numeric("gYear")

	@Serializable
	@SerialName("xsd\$primitive\$gYearMonth")
	open class GYearMonth : Numeric("gYearMonth")

	@Serializable
	@SerialName("xsd\$primitive\$NOTATION")
	open class Notation : Type("NOTATION"), Length, MinLength, MaxLength, Pattern, Enumeration, WhiteSpace {
		override var length: Int? = null
		override var minLength: Int? = null
		override var maxLength: Int? = null
		override var pattern: kotlin.String? = null
		override var values: MutableList<Data>? = null
		override var whiteSpace: kotlin.String? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$QName")
	open class QName : Type("QName"), Length, MinLength, MaxLength, Pattern, Enumeration, WhiteSpace {
		override var length: Int? = null
		override var minLength: Int? = null
		override var maxLength: Int? = null
		override var pattern: kotlin.String? = null
		override var values: MutableList<Data>? = null
		override var whiteSpace: kotlin.String? = null
	}

	@Serializable
	@SerialName("xsd\$primitive\$string")
	open class String : Type("string"), Length, MinLength, MaxLength, Pattern, Enumeration, WhiteSpace {
		override var length: Int? = null
		override var minLength: Int? = null
		override var maxLength: Int? = null
		override var pattern: kotlin.String? = null
		override var values: MutableList<Data>? = null
		override var whiteSpace: kotlin.String? = null
	}

	@Serializable
	@SerialName("time\$primitive\$time")
	open class Time : Numeric("time")

	private val primitives = mapOf(
		"anyURI" to { AnyUri() },
		"base64Binary" to { Base64Binary() },
		"boolean" to { Boolean() },
		"date" to { Date() },
		"dateTime" to { Datetime() },
		"decimal" to { Decimal() },
		"double" to { Double() },
		"duration" to { Duration() },
		"float" to { Float() },
		"hexBinary" to { HexBinary() },
		"gDay" to { GDay() },
		"gMonth" to { GMonth() },
		"gMonthDay" to { GMonthDay() },
		"gYear" to { GYear() },
		"gYearMonth" to { GYearMonth() },
		"NOTATION" to { Notation() },
		"QName" to { QName() },
		"string" to { String() },
		"time" to { Time() },
	)

	fun findPrimitive(name: kotlin.String, prefix: kotlin.String): Type? {
		return primitives[name.removePrefix("$prefix:")]?.invoke()
	}

	fun isPrimitive(name: kotlin.String) = primitives.keys.any { it == name }
}