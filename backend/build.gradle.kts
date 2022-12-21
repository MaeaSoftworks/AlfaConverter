val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
}

group = "com.maeasoftworks"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.2.1")
    implementation("io.ktor:ktor-server-core-jvm:2.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.2.1")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.2.1")
    implementation("io.ktor:ktor-server-default-headers-jvm:2.2.1")
    implementation("io.ktor:ktor-server-cors-jvm:2.2.1")
    implementation("io.ktor:ktor-server-host-common-jvm:2.2.1")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.2.1")
    implementation("io.ktor:ktor-server-netty-jvm:2.2.1")
    implementation("org.docx4j:docx4j-JAXB-ReferenceImpl:11.4.8")
    implementation("commons-codec:commons-codec:1.15") // override vulnerable docx4j dependency
    implementation("org.jdom:jdom2:2.0.6.1")
    implementation("org.apache.tika:tika-core:2.6.0")

    testImplementation("io.ktor:ktor-server-tests-jvm:2.2.1")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.2.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

val start by tasks.registering {
    dependsOn("run")
}
