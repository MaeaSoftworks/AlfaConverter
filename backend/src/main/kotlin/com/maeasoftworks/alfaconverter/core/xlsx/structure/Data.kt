package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.xlsx4j.sml.Cell

abstract class Data {
    abstract fun getXlsxRepresentation(): Cell

    abstract fun getJsonRepresentation(): String

    abstract fun getXmlRepresentation(): String

    abstract fun getString(): String
}
