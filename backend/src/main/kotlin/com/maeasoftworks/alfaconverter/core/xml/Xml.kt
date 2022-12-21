package com.maeasoftworks.alfaconverter.core.xml

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.model.Result
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.xml.structure.Element

class Xml(element: Element) : Result {
    private var schema: Schema = Schema(element)

    override var table: Table
        get() = schema.table
        set(value) {
            schema.table = value
        }

    override fun initialize(parent: Converter<*, *, *>) {}

    override fun convert() = schema.save().toByteArray()
}
