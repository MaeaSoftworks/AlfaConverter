package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.actions.Action
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class Conversion(val actions: @Contextual List<Action>) {
	fun register() {

	}

	fun start() {

	}
}