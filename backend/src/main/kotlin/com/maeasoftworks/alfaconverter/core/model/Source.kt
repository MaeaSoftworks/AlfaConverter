package com.maeasoftworks.alfaconverter.core.model

interface Source {
	val table: Table
	fun getHeaders(): List<String>
	fun getExamples(): List<String>
}