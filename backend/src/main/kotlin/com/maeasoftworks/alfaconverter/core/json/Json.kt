package com.maeasoftworks.alfaconverter.core.json

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.conversions.actions.Action
import com.maeasoftworks.alfaconverter.core.model.Result
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.plugins.serializer
import kotlinx.serialization.encodeToString

class Json : Result {
	override var table: Table = Table()

	override fun initialize(parent: Converter<*, *, *>) {
		parent.conversion.actions.add(
			object : Action() {
				override fun run(initialTable: Table, resultTable: Table) {
					table = initialTable
				}
			}
		)
	}

	override fun convert(): ByteArray {
		val result = mutableListOf<Map<String, String?>>()
		val headers = table.headers.map { it[0] }
		for (i in 0 until table.rowsCount) {
			val record = Table.slice(table.columns, i).map { it?.getJsonRepresentation() }
			result.add(headers.zip(record).toMap())
		}
		return serializer.encodeToString(result).toByteArray()
	}
}