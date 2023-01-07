package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.Converter
import com.maeasoftworks.alfaconverter.core.xlsx.BooleanData
import com.maeasoftworks.alfaconverter.core.xlsx.NumberData
import com.maeasoftworks.alfaconverter.core.xlsx.StringData
import com.maeasoftworks.alfaconverter.core.xlsx.Xlsx
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import java.nio.file.Files
import java.nio.file.Path

class DataRecognitionTests : FunSpec() {
    init {
        val converter = Converter(
            Xlsx(Files.readAllBytes(Path.of("src/test/resources/data recognition/source.xlsx"))),
            modifier = Xlsx()
        )
        val initial = converter.source.table

        test("size detection") {
            initial.values.size shouldBe 10
            initial.rowsCount shouldBe 1
            initial.headers.count() shouldBe 10
        }

        test("string detection") {
            with(initial[listOf("Фамилия"), 0]) {
                this should beInstanceOf<StringData>()
                this!!.getString() shouldBe "Иванов"
            }
        }

        test("number detection") {
            with(initial[listOf("Возраст"), 0]) {
                this should beInstanceOf<NumberData>()
                this!!.getString() shouldBe "59"
            }
        }

        test("date detection") {
            with(initial[listOf("Дата рождения"), 0]) {
                this should beInstanceOf<NumberData>()
                this!!.getString() shouldBe "21.06.1963"
            }
        }

        test("time detection with sec") {
            with(initial[listOf("Время взятия анализа"), 0]) {
                this should beInstanceOf<NumberData>()
                this!!.getString() shouldBe "9:20:00"
            }
        }

        test("time detection without sec") {
            with(initial[listOf("Время выполнения анализа"), 0]) {
                this should beInstanceOf<NumberData>()
                this!!.getString() shouldBe "13:53"
            }
        }

        test("date & time detection") {
            with(initial[listOf("Дата и время взятия анализа"), 0]) {
                this should beInstanceOf<NumberData>()
                this!!.getString() shouldBe "22.06.2022 9:20"
            }
        }

        test("boolean detection") {
            with(initial[listOf("Жив"), 0]) {
                this should beInstanceOf<BooleanData>()
                this!!.getString() shouldBe "false"
            }
        }
    }
}
