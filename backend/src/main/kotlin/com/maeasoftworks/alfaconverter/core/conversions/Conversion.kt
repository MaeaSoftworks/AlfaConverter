package com.maeasoftworks.alfaconverter.core.conversions

import com.maeasoftworks.alfaconverter.core.conversions.actions.Action
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Conversion(val actions: @Contextual MutableList<Action> = mutableListOf())