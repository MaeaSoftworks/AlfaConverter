package com.maeasoftworks.alfaconverter.models

import kotlinx.serialization.Serializable

@Serializable
data class XlsxPreviewResponse(val headers: List<String>, val examples: List<String>? = null)