package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.model.datatypes.XNumber
import com.maeasoftworks.alfaconverter.model.datatypes.XString
import com.maeasoftworks.alfaconverter.wrappers.Cell
import com.maeasoftworks.alfaconverter.wrappers.Table

internal object ExampleTables {
	val tableFrom: Table by lazy { Table().fill {
		header(Cell(0, 0) with XString("Column to bind 1"))
		put(Cell(1, 0) with XNumber(10))
		put(Cell(2, 0) with XNumber(20))
		put(Cell(3, 0) with XNumber(30))
		put(Cell(4, 0) with XNumber(40))
		put(Cell(5, 0) with XNumber(50))
		put(Cell(6, 0) with XNumber(60))
		put(Cell(7, 0) with XNumber(70))
		put(Cell(8, 0) with XNumber(80))
		put(Cell(9, 0) with XNumber(90))

		header(Cell(0, 1) with XString("Column to bind 2"))
		put(Cell(1, 1) with XNumber(100))
		put(Cell(2, 1) with XNumber(200))
		put(Cell(3, 1) with XNumber(300))
		put(Cell(4, 1) with XNumber(400))
		put(Cell(5, 1) with XNumber(500))
		put(Cell(6, 1) with XNumber(600))
		put(Cell(7, 1) with XNumber(700))
		put(Cell(8, 1) with XNumber(800))
		put(Cell(9, 1) with XNumber(900))

		header(Cell(0, 2) with XString("Will be split"))
		put(Cell(1, 2) with XString("a b c"))
		put(Cell(2, 2) with XString("b c a"))
		put(Cell(3, 2) with XString("c a b"))
		put(Cell(4, 2) with XString("a b c"))
		put(Cell(5, 2) with XString("b c a"))
		put(Cell(6, 2) with XString("c a b"))
		put(Cell(7, 2) with XString("a b c"))
		put(Cell(8, 2) with XString("b c a"))
		put(Cell(9, 2) with XString("c a b"))

		header(Cell(0, 3) with XString("Will"))
		put(Cell(1, 3) with XNumber(1))
		put(Cell(2, 3) with XNumber(3))
		put(Cell(3, 3) with XNumber(1))
		put(Cell(4, 3) with XNumber(3))
		put(Cell(5, 3) with XNumber(1))
		put(Cell(6, 3) with XNumber(3))
		put(Cell(7, 3) with XNumber(1))
		put(Cell(8, 3) with XNumber(3))
		put(Cell(9, 3) with XNumber(1))

		header(Cell(0, 4) with XString("be"))
		put(Cell(1, 4) with XNumber(2))
		put(Cell(2, 4) with XNumber(2))
		put(Cell(3, 4) with XNumber(2))
		put(Cell(4, 4) with XNumber(2))
		put(Cell(5, 4) with XNumber(2))
		put(Cell(6, 4) with XNumber(2))
		put(Cell(7, 4) with XNumber(2))
		put(Cell(8, 4) with XNumber(2))
		put(Cell(9, 4) with XNumber(2))

		header(Cell(0, 5) with XString("merged"))
		put(Cell(1, 5) with XNumber(3))
		put(Cell(2, 5) with XNumber(1))
		put(Cell(3, 5) with XNumber(3))
		put(Cell(4, 5) with XNumber(1))
		put(Cell(5, 5) with XNumber(3))
		put(Cell(6, 5) with XNumber(1))
		put(Cell(7, 5) with XNumber(3))
		put(Cell(8, 5) with XNumber(1))
		put(Cell(9, 5) with XNumber(3))

		header (Cell(0, 6) with XString("who?"))
		put(Cell(1, 6) with XString("me"))
		put(Cell(2, 6) with XString("me"))
		put(Cell(3, 6) with XString("me"))
		put(Cell(4, 6) with XString("me"))
		put(Cell(5, 6) with XString("me"))
		put(Cell(6, 6) with XString("me"))
		put(Cell(7, 6) with XString("me"))
		put(Cell(8, 6) with XString("me"))
		put(Cell(9, 6) with XString("me"))
	} }

	val tableTo: Table by lazy { Table().fill {
		header(Cell(0, 0) with XString("Column to bind 1"))
		header(Cell(0, 1) with XString("Column to bind 2"))
		header(Cell(0, 2) with XString("Will be split"))
		header(Cell(0, 3) with XString("Will"))
		header(Cell(0, 4) with XString("be"))
		header(Cell(0, 5) with XString("merged"))
		header(Cell(0, 6) with XString("who?"))
	} }
}