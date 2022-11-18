plugins {
	id("java")
	kotlin("jvm") version "1.7.20" apply false
}

allprojects {
	repositories {
		mavenCentral()
	}

	group = "com.maeasoftworks"
	version = "1"
}

val finalize by tasks.registering {
	dependsOn(subprojects.mapNotNull {
		when (it.name) {
			"frontend" -> it.tasks.findByName("stop")
			else -> null
		}
	})
}

val start by tasks.registering {
	dependsOn(subprojects.mapNotNull {
		when (it.name) {
			"backend" -> it.tasks.findByName("start")
			else -> null
		}
	})

	dependsOn(subprojects.mapNotNull {
		when (it.name) {
			"frontend" -> it.tasks.findByName("start")
			else -> null
		}
	})
}

start { finalizedBy(finalize) }