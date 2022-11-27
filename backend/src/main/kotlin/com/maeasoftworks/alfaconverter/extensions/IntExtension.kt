package com.maeasoftworks.alfaconverter.extensions

fun Int?.toBoolean(): Boolean {
	return this != 0
}