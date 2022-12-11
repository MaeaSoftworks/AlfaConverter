package com.maeasoftworks.alfaconverter.core

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import com.maeasoftworks.alfaconverter.core.model.Modifier
import com.maeasoftworks.alfaconverter.core.model.Result
import com.maeasoftworks.alfaconverter.core.model.Source
import org.jetbrains.annotations.TestOnly

class Converter<S : Source, M : Modifier?, R : Result?>(
	val source: S,
	val modifier: M,
	val result: R,
	val conversion: Conversion = Conversion()
) {
	fun executeActions() {
		for (action in conversion.actions.indices) {
			conversion.actions[0].run(source.table, result!!.table)
			conversion.actions.removeAt(0)
		}
	}

	fun initializeResultTable() = result?.initialize(modifier)

	fun convert(): ByteArray {
		initializeResultTable()
		executeActions()
		return result!!.convert()
	}
}

@TestOnly
fun <S : Source> Converter(
	source: S,
	conversion: Conversion = Conversion()
) = Converter(source, null, null, conversion)

fun <S : Source, R : Result?> Converter(
	source: S,
	result: R? = null,
	conversion: Conversion = Conversion()
) = Converter(source, null, result, conversion)

fun <S : Source, M : Modifier?> Converter(
	source: S,
	modifier: M? = null,
	conversion: Conversion = Conversion()
) = Converter(source, modifier, null, conversion)