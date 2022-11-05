package com.maeasoftworks.alfaconverter.wrappers

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage
import org.xlsx4j.sml.Worksheet

internal class Document {
	internal lateinit var document: SpreadsheetMLPackage
	internal lateinit var worksheet: Worksheet
	internal lateinit var table: Table
}