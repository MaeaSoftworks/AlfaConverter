package com.maeasoftworks.alfaconverter.modules

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*

class JsonRoutesTest : FunSpec() {
    init {
        test("[POST] api/json/convert") {
            testApplication {
                environment { config = ApplicationConfig("application.conf") }
                client.post("/api/json/convert") {
                    setBody(MultiPartFormDataContent(formData {
                        appendFile("source", "src/test/resources/routes/json", "source.xlsx", ContentType.Application.Xlsx)
                    }))
                }.bodyAsText() shouldBe """[{"ФИО":"Иванов Иван Иванович","Дата рождения":"21.06.1963","Возраст пациента":"59","Адрес прописки пациента":"Псков, ул. Ленина 35-28","Диагноз (код)":"К29.3","Диагноз (расшифровка)":"Гастрит","Тип исследования":"Гематологическое исследование","Адрес лаборатории":"Псков, 8 Марта, 23","Название лаборатории":"Клинико-диагностический центр","Код лаборатории":"145","Дата взятия анализа":"22.06.2022","Время взятия анализа":"9:20:00","Дата выполнения":"24.06.2022","Время выполнения анализа":"13:53"}]"""
            }
        }
    }
}