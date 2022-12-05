plugins {
	kotlin("jvm") version "1.7.22" apply false
}

allprojects {
	repositories {
		mavenCentral()
	}

	group = "com.maeasoftworks"
	version = "2"
}

val finalize by tasks.registering {
	dependsOn(project(":frontend").tasks.findByName("stop"))
}

val start by tasks.registering {
	dependsOn(project(":backend").tasks.findByName("start"))

	dependsOn(project(":frontend").tasks.findByName("start"))

	finalizedBy(finalize)
}