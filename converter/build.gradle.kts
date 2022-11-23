plugins {
	id("application")
	id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
	kotlin("jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.slf4j:slf4j-api:2.0.3")
	implementation("ch.qos.logback:logback-classic:1.4.4")

	implementation("org.docx4j:docx4j-JAXB-ReferenceImpl:11.4.8")
	implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
	implementation("jakarta.activation:jakarta.activation-api:2.1.0")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1")

	testImplementation(kotlin("test"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
	testImplementation("org.assertj:assertj-core:3.23.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}