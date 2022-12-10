package com.maeasoftworks.alfaconverter.utils

class NotNullableMap<K, V>(map: Map<K, V>) : HashMap<K, V>(map) {
	override operator fun get(key: K): V {
		return super.get(key) ?: throw NoSuchElementException("Element with key '$key' was not found")
	}
}