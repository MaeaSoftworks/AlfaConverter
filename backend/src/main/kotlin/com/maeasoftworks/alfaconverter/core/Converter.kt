package com.maeasoftworks.alfaconverter.core

/**
 * Main class for file conversions.
 * @param source object of [Source] with realization dependent on first input type.
 * @param modifier object of [Modifier] with realization dependent on second input file.
 * @param result object of [Result] with realization dependent on expected result file.
 * @param conversion [Conversion] object whose actions will be applied to the [source] table into the [result] table using the [modifier] table.
 * @constructor Creates a new instance of Converter.
 */
class Converter<S : Source, M : Modifier?, R : Result?>(
    val source: S,
    val modifier: M,
    val result: R,
    val conversion: Conversion = Conversion()
) {
    init {
        result?.initialize(this)
    }

    /**
     * Actually here goes on all table conversions. This function runs all actions in [conversion].
     */
    fun executeActions() {
        check(result != null) { "Conversion cannot run if the result is not specified" }
        for (action in conversion.actions.indices) {
            conversion.actions[0].run(source.table, result.table)
            conversion.actions.removeAt(0)
        }
    }

    /**
     * Saves all converted data.
     * @return conversion result as [ByteArray].
     */
    fun convert(): ByteArray {
        check(result != null) { "Conversion cannot work if the result is not specified" }
        executeActions()
        return result.convert()
    }
}

/**
 * Creates a new instance of Converter without [Modifier].
 * @param source object of [Source] with realization dependent on first input type.
 * @param result object of [Result] with realization dependent on expected result file.
 * @param conversion [Conversion] object whose actions will be applied to the [source] table into the [result] table without [Modifier] table.
 * @return New instance of Converter.
 */
fun <S : Source, R : Result> Converter(
    source: S,
    result: R,
    conversion: Conversion = Conversion()
) = Converter(source, null, result, conversion)

/**
 * Creates a new instance of Converter without [Result].
 * @param source object of [Source] with realization dependent on first input type.
 * @param modifier object of [Modifier] with realization dependent on second input file.
 * @return New instance of Converter.
 */
fun <S : Source, M : Modifier> Converter(
    source: S,
    modifier: M
) = Converter(source, modifier, null)
