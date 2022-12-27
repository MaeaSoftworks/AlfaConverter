package com.maeasoftworks.alfaconverter

import com.maeasoftworks.alfaconverter.core.xml.Schema
import com.maeasoftworks.alfaconverter.core.xml.structure.ComplexType
import com.maeasoftworks.alfaconverter.core.xml.structure.Primitive
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import java.io.File

class XsdTests : FunSpec() {
    init {
        val folder = "src/test/resources/xml"

        test("empty element recognized") {
            val schema = Schema(File("$folder/elementTest.xsd").readText())
            schema.elements.size shouldBe 1
            with(schema.elements[0]) {
                this.name shouldBe "test"
                this.type shouldBe null
            }
        }

        test("element with complex type and attribute recognized") {
            val schema = Schema(File("$folder/attributesTest.xsd").readText())
            schema.elements.size shouldBe 1
            with(schema.elements[0]) {
                this.type!!.attributes.size shouldBe 1
                with(this.type!!.attributes.keys.toList()[0]) {
                    this shouldBe "testAttribute"
                    type!!.attributes[this] should beInstanceOf<Primitive.String>()
                }
            }
        }

        test("element with complex type and element recognized") {
            val schema = Schema(File("$folder/elementsTest.xsd").readText())
            schema.elements.size shouldBe 1
            with(schema.elements[0]) {
                this.type!!.fields.size shouldBe 1
                with(this.type!!.fields.keys.toList()[0]) {
                    this shouldBe "testElement"
                    type!!.fields[this] should beInstanceOf<Primitive.String>()
                }
            }
        }

        test("element with ref element recognized") {
            val schema = Schema(File("$folder/refTest.xsd").readText())
            schema.elements.size shouldBe 2
            with(schema.elements[0]) {
                this.type!!.fields.size shouldBe 1
                with(this.type!!.fields.keys.toList()[0]) {
                    this shouldBe "person"
                    type!!.fields[this] should beInstanceOf<ComplexType>()
                }
            }
        }
    }
}
