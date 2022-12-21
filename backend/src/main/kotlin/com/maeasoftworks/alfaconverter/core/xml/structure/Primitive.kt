package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Primitive {
    @Serializable
    @SerialName("xsd:anyURI")
    object AnyUri : Type("anyURI")

    @Serializable
    @SerialName("xsd:base64Binary")
    object Base64Binary : Type("base64Binary")

    @Serializable
    @SerialName("xsd:boolean")
    object Boolean : Type("boolean")

    @Serializable
    @SerialName("xsd:date")
    object Date : Type("date")

    @Serializable
    @SerialName("xsd:dateTime")
    object Datetime : Type("dateTime")

    @Serializable
    @SerialName("xsd:decimal")
    object Decimal : Type("decimal")

    @Serializable
    @SerialName("xsd:double")
    object Double : Type("double")

    @Serializable
    @SerialName("xsd:duration")
    object Duration : Type("duration")

    @Serializable
    @SerialName("xsd:float")
    object Float : Type("float")

    @Serializable
    @SerialName("xsd:hexBinary")
    object HexBinary : Type("hexBinary")

    @Serializable
    @SerialName("xsd:gDay")
    object GDay : Type("gDay")

    @Serializable
    @SerialName("xsd:gMonth")
    object GMonth : Type("gMonth")

    @Serializable
    @SerialName("xsd:gMonthDay")
    object GMonthDay : Type("gMonthDay")

    @Serializable
    @SerialName("xsd:gYear")
    object GYear : Type("gYear")

    @Serializable
    @SerialName("xsd:gYearMonth")
    object GYearMonth : Type("gYearMonth")

    @Serializable
    @SerialName("xsd:NOTATION")
    object Notation : Type("NOTATION")

    @Serializable
    @SerialName("xsd:QName")
    object QName : Type("QName")

    @Serializable
    @SerialName("xsd:string")
    object String : Type("string")

    @Serializable
    @SerialName("time")
    object Time : Type("time")

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
