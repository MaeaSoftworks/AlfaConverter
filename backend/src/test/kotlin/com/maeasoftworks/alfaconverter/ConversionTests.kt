package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.Bind
import com.maeasoftworks.alfaconverter.core.Merge
import com.maeasoftworks.alfaconverter.core.Split
import com.maeasoftworks.alfaconverter.core.Table
import com.maeasoftworks.alfaconverter.core.xlsx.StringData
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ConversionTests : FunSpec() {
    init {
        test("bind works correctly") {
            val source = Table(
                listOf("Column to bind 1") to mutableListOf(StringData("a"), StringData("b"), StringData("c")),
                listOf("Column to bind 2") to mutableListOf(StringData("d"), StringData("e"), StringData("f"))
            )
            val result = Table(listOf("bind 1 here"), listOf("bind 2 here"))
            Bind(listOf("Column to bind 1"), listOf("bind 1 here")).run(source, result)
            Bind(listOf("Column to bind 2"), listOf("bind 2 here")).run(source, result)
            result[listOf("bind 1 here"), 0]!!.getString() shouldBe "a"
            result[listOf("bind 1 here"), 1]!!.getString() shouldBe "b"
            result[listOf("bind 1 here"), 2]!!.getString() shouldBe "c"

            result[listOf("bind 2 here"), 0]!!.getString() shouldBe "d"
            result[listOf("bind 2 here"), 1]!!.getString() shouldBe "e"
            result[listOf("bind 2 here"), 2]!!.getString() shouldBe "f"
        }

        test("bind skips null cells") {
            val source = Table(
                listOf("Column to bind 1") to mutableListOf(StringData("a"), null, StringData("c")),
                listOf("Column to bind 2") to mutableListOf(StringData("d"), null, StringData("f"))
            )
            val result = Table(listOf("bind 1 here"), listOf("bind 2 here"))
            Bind(listOf("Column to bind 1"), listOf("bind 1 here")).run(source, result)
            Bind(listOf("Column to bind 2"), listOf("bind 2 here")).run(source, result)
            result[listOf("bind 1 here"), 0]?.getString() shouldBe "a"
            result[listOf("bind 1 here"), 1]?.getString() shouldBe null
            result[listOf("bind 1 here"), 2]?.getString() shouldBe "c"

            result[listOf("bind 2 here"), 0]?.getString() shouldBe "d"
            result[listOf("bind 2 here"), 1]?.getString() shouldBe null
            result[listOf("bind 2 here"), 2]?.getString() shouldBe "f"
        }

        test("bind works with column smaller than average size of table") {
            val source = Table(
                listOf("Column to bind 1") to mutableListOf(StringData("a"), StringData("b"), StringData("c")),
                listOf("Column to bind 2") to mutableListOf(StringData("d"), StringData("f"))
            )
            val result = Table(listOf("bind 1 here"), listOf("bind 2 here"))
            Bind(listOf("Column to bind 1"), listOf("bind 1 here")).run(source, result)
            Bind(listOf("Column to bind 2"), listOf("bind 2 here")).run(source, result)
            result[listOf("bind 1 here"), 0]?.getString() shouldBe "a"
            result[listOf("bind 1 here"), 1]?.getString() shouldBe "b"
            result[listOf("bind 1 here"), 2]?.getString() shouldBe "c"

            result[listOf("bind 2 here"), 0]?.getString() shouldBe "d"
            result[listOf("bind 2 here"), 1]?.getString() shouldBe "f"
            result[listOf("bind 2 here"), 2]?.getString() shouldBe null
        }

        test("split works correctly") {
            val source = Table(listOf("split me plz") to mutableListOf(StringData("abc"), StringData("bcd"), StringData("cde")))
            val result = Table(listOf("split"), listOf("me"), listOf("plz"))
            Split(listOf("split me plz"), listOf(listOf("split"), listOf("me"), listOf("plz")), "(.)(.)(.)").run(source, result)
            result[listOf("split"), 0]!!.getString() shouldBe "a"
            result[listOf("split"), 1]!!.getString() shouldBe "b"
            result[listOf("split"), 2]!!.getString() shouldBe "c"

            result[listOf("me"), 0]!!.getString() shouldBe "b"
            result[listOf("me"), 1]!!.getString() shouldBe "c"
            result[listOf("me"), 2]!!.getString() shouldBe "d"

            result[listOf("plz"), 0]!!.getString() shouldBe "c"
            result[listOf("plz"), 1]!!.getString() shouldBe "d"
            result[listOf("plz"), 2]!!.getString() shouldBe "e"
        }

        test("split skips null values") {
            val source = Table(listOf("split me plz") to mutableListOf(StringData("abc"), null, StringData("cde")))
            val result = Table(listOf("split"), listOf("me"), listOf("plz"))
            Split(listOf("split me plz"), listOf(listOf("split"), listOf("me"), listOf("plz")), "(.)(.)(.)").run(source, result)
            result[listOf("split"), 0]!!.getString() shouldBe "a"
            result[listOf("split"), 1]?.getString() shouldBe null
            result[listOf("split"), 2]!!.getString() shouldBe "c"

            result[listOf("me"), 0]!!.getString() shouldBe "b"
            result[listOf("me"), 1]?.getString() shouldBe null
            result[listOf("me"), 2]!!.getString() shouldBe "d"

            result[listOf("plz"), 0]!!.getString() shouldBe "c"
            result[listOf("plz"), 1]?.getString() shouldBe null
            result[listOf("plz"), 2]!!.getString() shouldBe "e"
        }

        test("split works with column smaller than average size of table") {
            val source = Table(
                listOf("split me plz") to mutableListOf(StringData("abc"), StringData("cde")),
                listOf("big brother") to mutableListOf(StringData("1"), StringData("2"), StringData("3"))
            )
            val result = Table(listOf("split"), listOf("me"), listOf("plz"))
            Split(listOf("split me plz"), listOf(listOf("split"), listOf("me"), listOf("plz")), "(.)(.)(.)").run(source, result)
            result[listOf("split"), 0]!!.getString() shouldBe "a"
            result[listOf("split"), 1]!!.getString() shouldBe "c"
            result[listOf("split"), 2]?.getString() shouldBe null

            result[listOf("me"), 0]!!.getString() shouldBe "b"
            result[listOf("me"), 1]!!.getString() shouldBe "d"
            result[listOf("me"), 2]?.getString() shouldBe null

            result[listOf("plz"), 0]!!.getString() shouldBe "c"
            result[listOf("plz"), 1]!!.getString() shouldBe "e"
            result[listOf("plz"), 2]?.getString() shouldBe null
        }

        test("merge works correctly") {
            val source = Table(
                listOf("merge") to mutableListOf(StringData("a"), StringData("b"), StringData("c")),
                listOf("me") to mutableListOf(StringData("b"), StringData("c"), StringData("d")),
                listOf("plz") to mutableListOf(StringData("c"), StringData("d"), StringData("e"))
            )
            val result = Table(listOf("merge me plz"))
            Merge(listOf(listOf("merge"), listOf("me"), listOf("plz")), listOf("merge me plz"), "\${0}\${1}\${2}").run(source, result)
            result[listOf("merge me plz"), 0]!!.getString() shouldBe "abc"
            result[listOf("merge me plz"), 1]!!.getString() shouldBe "bcd"
            result[listOf("merge me plz"), 2]!!.getString() shouldBe "cde"
        }

        test("merge skips null values as empty strings") {
            val source = Table(
                listOf("merge") to mutableListOf(StringData("a"), null, StringData("c")),
                listOf("me") to mutableListOf(StringData("b"), null, StringData("d")),
                listOf("plz") to mutableListOf(StringData("c"), null, StringData("e"))
            )
            val result = Table(listOf("merge me plz"))
            Merge(listOf(listOf("merge"), listOf("me"), listOf("plz")), listOf("merge me plz"), "\${0}\${1}\${2}").run(source, result)
            result[listOf("merge me plz"), 0]!!.getString() shouldBe "abc"
            result[listOf("merge me plz"), 1]!!.getString() shouldBe ""
            result[listOf("merge me plz"), 2]!!.getString() shouldBe "cde"
        }

        test("merge works with column smaller than average size of table") {
            val source = Table(
                listOf("merge") to mutableListOf(StringData("a"), StringData("b"), StringData("c")),
                listOf("me") to mutableListOf(StringData("b"), StringData("d")),
                listOf("plz") to mutableListOf(StringData("c"), StringData("e"))
            )
            val result = Table(listOf("merge me plz"))
            Merge(listOf(listOf("merge"), listOf("me"), listOf("plz")), listOf("merge me plz"), "\${0}\${1}\${2}").run(source, result)
            result[listOf("merge me plz"), 0]!!.getString() shouldBe "abc"
            result[listOf("merge me plz"), 1]!!.getString() shouldBe "bde"
            result[listOf("merge me plz"), 2]!!.getString() shouldBe "c"
        }
    }
}
