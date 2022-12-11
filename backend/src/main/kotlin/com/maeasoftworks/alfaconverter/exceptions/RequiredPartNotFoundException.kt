package com.maeasoftworks.alfaconverter.exceptions

class RequiredPartNotFoundException(expected: String, actual: String) :
	Exception("Inconsistent form-data parts: expected: [$expected], actual: [$actual]")