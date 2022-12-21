package com.maeasoftworks.alfaconverter.core.xlsx.structure

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.docx4j.openpackaging.parts.PartName
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings
import org.xlsx4j.sml.CTRst
import org.xlsx4j.sml.CTXstringWhitespace
import org.xlsx4j.sml.Cell
import org.xlsx4j.sml.STCellType

data class StringData(private val value: String) : Data() {
    constructor(spreadsheet: SpreadsheetMLPackage, cell: Cell) : this((spreadsheet.parts[sharedStringsPart] as SharedStrings).jaxbElement.si[cell.v.toInt()].t.value)

    override fun getXlsxRepresentation() = Cell().apply {
        t = STCellType.INLINE_STR
        `is` = CTRst().apply { t = CTXstringWhitespace().apply { value = this@StringData.value } }
    }

    override fun getJsonRepresentation() = value

    override fun getXmlRepresentation() = "\"$value\""

    override fun getString() = value

    companion object {
        private val sharedStringsPart = PartName("/xl/sharedStrings.xml")
    }
}
