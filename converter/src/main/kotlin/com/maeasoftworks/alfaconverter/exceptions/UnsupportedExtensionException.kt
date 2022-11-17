package com.maeasoftworks.alfaconverter.exceptions

class UnsupportedExtensionException(extension: String) : Exception("Unsupported file extension: $extension")