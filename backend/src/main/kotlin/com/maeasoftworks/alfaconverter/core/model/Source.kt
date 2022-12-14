package com.maeasoftworks.alfaconverter.core.model

interface Source {
	val table: Table
	fun getHeaders(): List<ColumnAddress>
	fun getExamples(): List<String>
}