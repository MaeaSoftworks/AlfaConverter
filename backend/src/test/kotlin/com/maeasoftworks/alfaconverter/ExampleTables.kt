package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SNumber
import com.maeasoftworks.alfaconverter.core.datatypes.xlsx.SString
import com.maeasoftworks.alfaconverter.core.model.Table

internal object ExampleTables {
	val tableFrom: Table by lazy {
		Table().fill {
			column(SString("Column to bind 1")) {
				+SNumber(10)
				+SNumber(20)
				+SNumber(30)
				+SNumber(40)
				+SNumber(50)
				+SNumber(60)
				+SNumber(70)
				+SNumber(80)
				+SNumber(90)
			}
			column(SString("Column to bind 2")) {
				+SNumber(100)
				+SNumber(200)
				+SNumber(300)
				+SNumber(400)
				+SNumber(500)
				+SNumber(600)
				+SNumber(700)
				+SNumber(800)
				+SNumber(900)
			}
			column(SString("Will be split")) {
				+SString("a b c")
				+SString("b c a")
				+SString("c a b")
				+SString("a b c")
				+SString("b c a")
				+SString("c a b")
				+SString("a b c")
				+SString("b c a")
				+SString("c a b")
			}
			column(SString("Will")) {
				+SNumber(1)
				+SNumber(3)
				+SNumber(1)
				+SNumber(3)
				+SNumber(1)
				+SNumber(3)
				+SNumber(1)
				+SNumber(3)
				+SNumber(1)
			}
			column(SString("be")) {
				+SNumber(2)
				+SNumber(2)
				+SNumber(2)
				+SNumber(2)
				+SNumber(2)
				+SNumber(2)
				+SNumber(2)
				+SNumber(2)
				+SNumber(2)
			}
			column(SString("merged")) {
				+SNumber(3)
				+SNumber(1)
				+SNumber(3)
				+SNumber(1)
				+SNumber(3)
				+SNumber(1)
				+SNumber(3)
				+SNumber(1)
				+SNumber(3)
			}
			column(SString("who?")) {
				+SString("me")
				+SString("me")
				+SString("me")
				+SString("me")
				+SString("me")
				+SString("me")
				+SString("me")
				+SString("me")
				+SString("me")
			}
		}
	}

	val tableTo: Table by lazy {
		Table().fill {
			column(SString("Column to bind 1"))
			column(SString("Column to bind 2"))
			column(SString("Will be merged"))
			column(SString("Will"))
			column(SString("be"))
			column(SString("split"))
			column(SString("who?"))
		}
	}
}