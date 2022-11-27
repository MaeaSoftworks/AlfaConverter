plugins {
	id("application")
	id("org.springframework.boot") version "2.7.6"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
	kotlin("plugin.jpa") version "1.7.20"
	kotlin("plugin.spring") version "1.7.20"
	kotlin("jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_17

application {
	mainClass.set("com.maeasoftworks.alfaconverter.AlfaConverterApplicationKt")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// kotlin
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// serialization
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
	implementation("org.jdom:jdom2:2.0.6.1")

	// docx4j
	implementation("org.docx4j:docx4j-JAXB-ReferenceImpl:11.4.8")
	implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
	implementation("jakarta.activation:jakarta.activation-api:2.1.0")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

	// spring
	implementation("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(kotlin("test"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val start by tasks.registering {
	dependsOn("bootRun")
}