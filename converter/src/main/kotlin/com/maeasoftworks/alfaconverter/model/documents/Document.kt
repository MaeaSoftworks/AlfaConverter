package com.maeasoftworks.alfaconverter.model.documents

import com.maeasoftworks.alfaconverter.wrappers.Table

internal abstract class Document {
	internal lateinit var table: Table
	internal val isTableInitialized: Boolean
		get() = ::table.isInitialized
	abstract fun open(file: ByteArray): Document
	abstract fun save(): ByteArray
	abstract fun initializeTable()
	abstract fun getHeadersAndExamples(): List<List<String?>>
	abstract fun clean()
}