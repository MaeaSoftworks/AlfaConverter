plugins {
	id("application")
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("java")
	id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
	kotlin("jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_17

application {
	mainClass.set("com.maeasoftworks.alfaconverterapi.AlfaConverterApiApplication")
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
	// converter
	implementation(project(":converter"))
	testImplementation(project(":converter"))
	// required minimum for docx4j from converter
	implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
	implementation("jakarta.activation:jakarta.activation-api:2.1.0")


	// kotlin
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1")

	// spring
	implementation("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// utils
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("commons-io:commons-io:2.11.0")

	// lombok
	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")
	testCompileOnly("org.projectlombok:lombok:1.18.24")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

	// testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val start by tasks.registering {
	dependsOn("bootRun")
}