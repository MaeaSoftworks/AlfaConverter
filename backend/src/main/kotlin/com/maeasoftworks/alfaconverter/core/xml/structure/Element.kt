package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.Serializable

@Serializable
class Element(val name: String) {
	lateinit var type: Type

	fun createInstance() = type.createInstance()
}