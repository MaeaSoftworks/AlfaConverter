package com.maeasoftworks.alfaconverter.modules

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.config.*
import io.ktor.server.testing.*

class StatusPagesTest : FunSpec() {
    init {
        test("custom 404") {
            testApplication {
                environment { config = ApplicationConfig("application.conf") }
                client.post("/api/not-exist").bodyAsText() shouldBe """{"code":"404","message":"Route not found"}"""
            }
        }
    }
}