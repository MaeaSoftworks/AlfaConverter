package com.maeasoftworks.alfaconverter.core.xlsx

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.model.Modifier
import com.maeasoftworks.alfaconverter.core.model.Result
import com.maeasoftworks.alfaconverter.core.model.Source
import com.maeasoftworks.alfaconverter.core.model.Table

class Xlsx : Source, Modifier, Result {

    // all
    private var spreadsheet: Spreadsheet = Spreadsheet(Table())

    // source & result
    override var table: Table
        get() = spreadsheet.table
        set(value) {
            spreadsheet.table = value
        }

    constructor()

    constructor(file: ByteArray) {
        this.spreadsheet = Spreadsheet(file)
    }

    // source & modifier
    override fun getHeaders() = spreadsheet.getHeaders()

    // source
    override fun getExamples() = spreadsheet.getExamples()

    // modifier
    override fun getPayload() = null

    override fun initialize(parent: Converter<*, *, *>) {
        check(parent.modifier != null) { "Conversion cannot work if the result is not specified" }
        table = Table()
        for (column in parent.modifier.getHeaders()) {
            table.add(column)
        }
    }

    // result
    override fun convert() = spreadsheet.save()
}
