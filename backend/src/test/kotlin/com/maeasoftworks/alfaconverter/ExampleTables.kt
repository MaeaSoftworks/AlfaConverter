package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.conversions.pos
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SNumber
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.model.Table
import com.maeasoftworks.alfaconverter.core.model.Table.*

internal object ExampleTables {
	val tableFrom: Table by lazy { Table().fill {
		header(Cell(0.pos, 0) with SString("Column to bind 1"))
		put(Cell(0.pos, 1) with SNumber(10))
		put(Cell(0.pos, 2) with SNumber(20))
		put(Cell(0.pos, 3) with SNumber(30))
		put(Cell(0.pos, 4) with SNumber(40))
		put(Cell(0.pos, 5) with SNumber(50))
		put(Cell(0.pos, 6) with SNumber(60))
		put(Cell(0.pos, 7) with SNumber(70))
		put(Cell(0.pos, 8) with SNumber(80))
		put(Cell(0.pos, 9) with SNumber(90))

		header(Cell(1.pos, 0) with SString("Column to bind 2"))
		put(Cell(1.pos, 1) with SNumber(100))
		put(Cell(1.pos, 2) with SNumber(200))
		put(Cell(1.pos, 3) with SNumber(300))
		put(Cell(1.pos, 4) with SNumber(400))
		put(Cell(1.pos, 5) with SNumber(500))
		put(Cell(1.pos, 6) with SNumber(600))
		put(Cell(1.pos, 7) with SNumber(700))
		put(Cell(1.pos, 8) with SNumber(800))
		put(Cell(1.pos, 9) with SNumber(900))

		header(Cell(2.pos, 0) with SString("Will be split"))
		put(Cell(2.pos, 1) with SString("a b c"))
		put(Cell(2.pos, 2) with SString("b c a"))
		put(Cell(2.pos, 3) with SString("c a b"))
		put(Cell(2.pos, 4) with SString("a b c"))
		put(Cell(2.pos, 5) with SString("b c a"))
		put(Cell(2.pos, 6) with SString("c a b"))
		put(Cell(2.pos, 7) with SString("a b c"))
		put(Cell(2.pos, 8) with SString("b c a"))
		put(Cell(2.pos, 9) with SString("c a b"))

		header(Cell(3.pos, 0) with SString("Will"))
		put(Cell(3.pos, 1) with SNumber(1))
		put(Cell(3.pos, 2) with SNumber(3))
		put(Cell(3.pos, 3) with SNumber(1))
		put(Cell(3.pos, 4) with SNumber(3))
		put(Cell(3.pos, 5) with SNumber(1))
		put(Cell(3.pos, 6) with SNumber(3))
		put(Cell(3.pos, 7) with SNumber(1))
		put(Cell(3.pos, 8) with SNumber(3))
		put(Cell(3.pos, 9) with SNumber(1))

		header(Cell(4.pos, 0) with SString("be"))
		put(Cell(4.pos, 1) with SNumber(2))
		put(Cell(4.pos, 2) with SNumber(2))
		put(Cell(4.pos, 3) with SNumber(2))
		put(Cell(4.pos, 4) with SNumber(2))
		put(Cell(4.pos, 5) with SNumber(2))
		put(Cell(4.pos, 6) with SNumber(2))
		put(Cell(4.pos, 7) with SNumber(2))
		put(Cell(4.pos, 8) with SNumber(2))
		put(Cell(4.pos, 9) with SNumber(2))

		header(Cell(5.pos, 0) with SString("merged"))
		put(Cell(5.pos, 1) with SNumber(3))
		put(Cell(5.pos, 2) with SNumber(1))
		put(Cell(5.pos, 3) with SNumber(3))
		put(Cell(5.pos, 4) with SNumber(1))
		put(Cell(5.pos, 5) with SNumber(3))
		put(Cell(5.pos, 6) with SNumber(1))
		put(Cell(5.pos, 7) with SNumber(3))
		put(Cell(5.pos, 8) with SNumber(1))
		put(Cell(5.pos, 9) with SNumber(3))

		header(Cell(6.pos, 0) with SString("who?"))
		put(Cell(6.pos, 1) with SString("me"))
		put(Cell(6.pos, 2) with SString("me"))
		put(Cell(6.pos, 3) with SString("me"))
		put(Cell(6.pos, 4) with SString("me"))
		put(Cell(6.pos, 5) with SString("me"))
		put(Cell(6.pos, 6) with SString("me"))
		put(Cell(6.pos, 7) with SString("me"))
		put(Cell(6.pos, 8) with SString("me"))
		put(Cell(6.pos, 9) with SString("me"))
	} }

	val tableTo: Table by lazy { Table().fill {
		header(Cell(0.pos, 0) with SString("Column to bind 1"))
		header(Cell(1.pos, 0) with SString("Column to bind 2"))
		header(Cell(2.pos, 0) with SString("Will be split"))
		header(Cell(3.pos, 0) with SString("Will"))
		header(Cell(4.pos, 0) with SString("be"))
		header(Cell(5.pos, 0) with SString("merged"))
		header(Cell(6.pos, 0) with SString("who?"))
	} }
}