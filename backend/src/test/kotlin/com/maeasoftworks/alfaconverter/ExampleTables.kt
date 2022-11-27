package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SNumber
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*

internal object ExampleTables {
	val tableFrom: Table by lazy { Table().fill {
		header(Cell(0, 0) with SString("Column to bind 1"))
		put(Cell(1, 0) with SNumber(10))
		put(Cell(2, 0) with SNumber(20))
		put(Cell(3, 0) with SNumber(30))
		put(Cell(4, 0) with SNumber(40))
		put(Cell(5, 0) with SNumber(50))
		put(Cell(6, 0) with SNumber(60))
		put(Cell(7, 0) with SNumber(70))
		put(Cell(8, 0) with SNumber(80))
		put(Cell(9, 0) with SNumber(90))

		header(Cell(0, 1) with SString("Column to bind 2"))
		put(Cell(1, 1) with SNumber(100))
		put(Cell(2, 1) with SNumber(200))
		put(Cell(3, 1) with SNumber(300))
		put(Cell(4, 1) with SNumber(400))
		put(Cell(5, 1) with SNumber(500))
		put(Cell(6, 1) with SNumber(600))
		put(Cell(7, 1) with SNumber(700))
		put(Cell(8, 1) with SNumber(800))
		put(Cell(9, 1) with SNumber(900))

		header(Cell(0, 2) with SString("Will be split"))
		put(Cell(1, 2) with SString("a b c"))
		put(Cell(2, 2) with SString("b c a"))
		put(Cell(3, 2) with SString("c a b"))
		put(Cell(4, 2) with SString("a b c"))
		put(Cell(5, 2) with SString("b c a"))
		put(Cell(6, 2) with SString("c a b"))
		put(Cell(7, 2) with SString("a b c"))
		put(Cell(8, 2) with SString("b c a"))
		put(Cell(9, 2) with SString("c a b"))

		header(Cell(0, 3) with SString("Will"))
		put(Cell(1, 3) with SNumber(1))
		put(Cell(2, 3) with SNumber(3))
		put(Cell(3, 3) with SNumber(1))
		put(Cell(4, 3) with SNumber(3))
		put(Cell(5, 3) with SNumber(1))
		put(Cell(6, 3) with SNumber(3))
		put(Cell(7, 3) with SNumber(1))
		put(Cell(8, 3) with SNumber(3))
		put(Cell(9, 3) with SNumber(1))

		header(Cell(0, 4) with SString("be"))
		put(Cell(1, 4) with SNumber(2))
		put(Cell(2, 4) with SNumber(2))
		put(Cell(3, 4) with SNumber(2))
		put(Cell(4, 4) with SNumber(2))
		put(Cell(5, 4) with SNumber(2))
		put(Cell(6, 4) with SNumber(2))
		put(Cell(7, 4) with SNumber(2))
		put(Cell(8, 4) with SNumber(2))
		put(Cell(9, 4) with SNumber(2))

		header(Cell(0, 5) with SString("merged"))
		put(Cell(1, 5) with SNumber(3))
		put(Cell(2, 5) with SNumber(1))
		put(Cell(3, 5) with SNumber(3))
		put(Cell(4, 5) with SNumber(1))
		put(Cell(5, 5) with SNumber(3))
		put(Cell(6, 5) with SNumber(1))
		put(Cell(7, 5) with SNumber(3))
		put(Cell(8, 5) with SNumber(1))
		put(Cell(9, 5) with SNumber(3))

		header (Cell(0, 6) with SString("who?"))
		put(Cell(1, 6) with SString("me"))
		put(Cell(2, 6) with SString("me"))
		put(Cell(3, 6) with SString("me"))
		put(Cell(4, 6) with SString("me"))
		put(Cell(5, 6) with SString("me"))
		put(Cell(6, 6) with SString("me"))
		put(Cell(7, 6) with SString("me"))
		put(Cell(8, 6) with SString("me"))
		put(Cell(9, 6) with SString("me"))
	} }

	val tableTo: Table by lazy { Table().fill {
		header(Cell(0, 0) with SString("Column to bind 1"))
		header(Cell(0, 1) with SString("Column to bind 2"))
		header(Cell(0, 2) with SString("Will be split"))
		header(Cell(0, 3) with SString("Will"))
		header(Cell(0, 4) with SString("be"))
		header(Cell(0, 5) with SString("merged"))
		header(Cell(0, 6) with SString("who?"))
	} }
}