package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.conversions.Conversion
import com.maeasoftworks.alfaconverter.conversions.TypeConversion
import com.maeasoftworks.alfaconverter.conversions.actions.Bind
import com.maeasoftworks.alfaconverter.conversions.actions.Merge
import com.maeasoftworks.alfaconverter.conversions.actions.Split
import com.maeasoftworks.alfaconverter.model.datatypes.XTypeName
import com.maeasoftworks.alfaconverter.model.documents.XlsxDocument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

fun main() {
	val conversion = Conversion(
		mutableListOf(
			Bind(0, 1),
			Bind(1, 0),
			Split(2, listOf(2, 3, 4), "(\\S+) (\\S+) (\\S+)"),
			Merge(listOf(3, 4, 5), 5, "$3 $4 $5")
		),
		mutableMapOf(
			2 to TypeConversion(XTypeName.XNumber, 0),
			3 to TypeConversion(XTypeName.XNumber, 0),
			4 to TypeConversion(XTypeName.XNumber, 0)
		)
	)
	println("Select action:\n" +
			"1. Generate conversion example\n" +
			"2. Generate xlsx example tables")
	val action = readln().toInt()
	if (action == 1) {
		println(Json.encodeToString(conversion))
	}
	if (action == 2) {
		println("Select folder for files or leave empty to save to ${Path("").absolutePathString()}")
		val path = readln()
		var converter = Converter()
		converter.documents.first = XlsxDocument().also { it.table = ExampleTables.tableFrom }
		converter.documents.second = XlsxDocument().also { it.table = ExampleTables.tableFrom }
		converter.initialize()
		Files.write(Path("${path}/source.xlsx"), converter.convert())
		converter = Converter()
		converter.documents.first = XlsxDocument().also { it.table = ExampleTables.tableTo }
		converter.documents.second = XlsxDocument().also { it.table = ExampleTables.tableTo }
		converter.initialize()
		Files.write(Path("${path}/target.xlsx"), converter.convert())
		println("Saved in ${Path(path).absolutePathString()}")
	}
}