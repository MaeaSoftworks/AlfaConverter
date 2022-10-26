import com.github.gradle.node.npm.task.NpmTask

plugins {
	id("idea")
	id("com.github.node-gradle.node") version "3.5.0"
}

repositories {
	mavenCentral()
	gradlePluginPortal()
}

buildscript {
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}

	dependencies {
		classpath("com.github.node-gradle:gradle-node-plugin:3.5.0")
	}
}

node {
	version.set("16.14.0")
	npmVersion.set("8.3.1")
	download.set(false)
	workDir.set(file("${project.buildDir}/node"))
	nodeProjectDir.set(file("${project.projectDir}"))
}

val installDependencies by tasks.registering(NpmTask::class) {
	args.set(listOf("install"))
}

val build by tasks.registering(NpmTask::class) {
	dependsOn(installDependencies)
	args.set(listOf("run", "build"))
}

val start by tasks.registering(NpmTask::class) {
	args.set(listOf("start"))
}

val stop by tasks.registering(NpmTask::class) {
	args.set(listOf("stop"))
}