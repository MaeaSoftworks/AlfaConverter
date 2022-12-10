package com.maeasoftworks.alfaconverter.exceptions

class RequiredPartNotFoundException(partName: String) :
	Exception("Required part '$partName' was not found in form-data parts")