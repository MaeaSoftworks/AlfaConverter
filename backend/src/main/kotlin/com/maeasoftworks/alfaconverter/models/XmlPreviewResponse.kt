package com.maeasoftworks.alfaconverter.models

import com.maeasoftworks.alfaconverter.core.xml.structure.Element
import kotlinx.serialization.Serializable

@Serializable
data class XmlPreviewResponse(
	var headers: List<String>,
	var examples: List<String>,
	var schema: Element,
	var endpoints: List<String>
)