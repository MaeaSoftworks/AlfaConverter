package com.maeasoftworks.alfaconverter.core.xml.structure

import kotlinx.serialization.Serializable

@Serializable
class UnknownType : Type {
	constructor(name: String) : super(name)
}