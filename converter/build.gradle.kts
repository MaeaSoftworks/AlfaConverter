plugins {
	id("application")
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

	implementation("org.glassfish.jaxb:jaxb-runtime:4.0.0")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
	implementation("jakarta.activation:jakarta.activation-api:2.1.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}