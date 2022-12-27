package com.maeasoftworks.alfaconverter.modules

import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

fun FormBuilder.appendFile(key: String, folder: String, filename: String, contentType: ContentType) = append(
    key,
    File("$folder/$filename").readBytes(),
    Headers.build {
        append(HttpHeaders.ContentType, contentType)
        append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
    }
)