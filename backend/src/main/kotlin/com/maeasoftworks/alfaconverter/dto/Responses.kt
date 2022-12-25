package com.maeasoftworks.alfaconverter.dto

import com.maeasoftworks.alfaconverter.core.ColumnAddress
import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import kotlinx.serialization.Serializable

@Serializable
data class XlsxPreviewResponse(val headers: List<ColumnAddress>, val examples: List<String>? = null)

@Serializable
data class XmlPreviewResponse(
    var headers: List<ColumnAddress>,
    var examples: List<String>,
    var schema: Element,
    var endpoints: List<ColumnAddress>
)
