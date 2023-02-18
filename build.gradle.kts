plugins {
    kotlin("jvm") version "1.7.22" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "com.maeasoftworks"
    version = "1"
}

val start by tasks.registering {
    dependsOn(project(":frontend").tasks.findByName("start"))
    dependsOn(project(":backend").tasks.findByName("start"))
    finalizedBy(project(":frontend").tasks.findByName("stop"))
}