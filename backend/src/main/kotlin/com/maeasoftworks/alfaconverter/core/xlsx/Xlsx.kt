package com.maeasoftworks.alfaconverter.core.xlsx

import com.maeasoftworks.alfaconverter.core.*

class Xlsx : Source, Modifier, Result {
    private var spreadsheet: Spreadsheet = Spreadsheet(Table())

    override val table: Table
        get() = spreadsheet.table

    constructor()

    constructor(file: ByteArray) {
        this.spreadsheet = Spreadsheet(file)
    }

    override fun getHeaders() = spreadsheet.getHeaders()

    override fun getExamples() = spreadsheet.getExamples()

    override fun getPayload() = null

    override fun initialize(parent: Converter<*, *, *>) {
        check(parent.modifier != null) { "Conversion cannot work if the modifier is not specified" }
        spreadsheet.table = Table()
        for (column in parent.modifier.getHeaders()) {
            table.add(column)
        }
    }

    override fun convert() = spreadsheet.save()
}
