package com.maeasoftworks.alfaconverter.documents

import com.maeasoftworks.alfaconverter.wrappers.Table

internal abstract class Document {
	internal lateinit var table: Table
	abstract fun open(file: ByteArray): Document
	abstract fun save(): ByteArray
	abstract fun createTable(): Table
	abstract fun getHeaders(): List<String?>
}