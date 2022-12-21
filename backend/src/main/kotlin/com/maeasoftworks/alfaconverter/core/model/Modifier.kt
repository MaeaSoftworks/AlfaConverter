package com.maeasoftworks.alfaconverter.core.model

/**
 * Abstract representation of modifier file.
 */
interface Modifier {
	/**
	 * Retrieves all addresses from table.
	 * @return list of addresses.
	 */
	fun getHeaders(): List<ColumnAddress>

	/**
	 * Retrieves payload (type-specific).
	 * @return payload.
	 */
	fun getPayload(): Any?
}