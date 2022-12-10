package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.model.Table

abstract class Converter {
	var conversion: Conversion = Conversion()
	abstract val source: Table
	abstract val target: Table

	fun executeActions() {
		for (action in conversion.actions.indices) {
			conversion.actions[0].run(source, target)
			conversion.actions.removeAt(0)
		}
	}
}