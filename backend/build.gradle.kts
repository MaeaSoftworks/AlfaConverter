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
	implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
	implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
	implementation("ch.qos.logback:logback-classic:$logbackVersion")
	testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

	// docx4j
	implementation("org.docx4j:docx4j-JAXB-ReferenceImpl:11.4.8")
	implementation("commons-codec:commons-codec:1.15") // override vulnerable docx4j dependency
	//implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
	//implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
	//implementation("jakarta.activation:jakarta.activation-api:2.1.0")
	//implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

	implementation("org.jdom:jdom2:2.0.6.1")
	implementation("org.apache.tika:tika-core:2.6.0")
}

val start by tasks.registering {
	dependsOn("run")
}