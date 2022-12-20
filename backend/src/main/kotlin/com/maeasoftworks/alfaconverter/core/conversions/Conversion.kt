package com.maeasoftworks.alfaconverter.core.conversions

import com.maeasoftworks.alfaconverter.core.conversions.actions.Action
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Holds all actions that will be applied to source table.
 * @param actions actions that will be applied to source table.
 * @constructor Creates a new instance.
 */
@Serializable
data class Conversion(val actions: @Contextual MutableList<Action> = mutableListOf())