package com.maeasoftworks.alfaconverter.dao

import com.maeasoftworks.alfaconverter.core.conversions.Conversion
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "logs")
class Log(
	@Column
	val timestamp: LocalDateTime,
	@Lob
	@Column
	val conversion: String,
	@Column
	val resultCode: Int
) {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	val id: Long = 0

	constructor(timestamp: LocalDateTime, conversion: Conversion, resultCode: Int) : this(timestamp, Json.encodeToString(conversion), resultCode)
}