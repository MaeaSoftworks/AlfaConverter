plugins {
	id("idea")
	id("com.github.node-gradle.node")
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
	version = '19.0.0'
	npmVersion = '8.15.0'
	download = false
	workDir = file("${project.buildDir}/node")
	nodeModulesDir = file("${project.projectDir}/node_modules")
}

/**
 * Runs "npm run build" to build the angular app.
 */
fun Task build(type: NpmTask) {
	args = ['run', 'build']
}
build.dependsOn(npm_install)

/**
 * Deletes the "dist" folder containing the result of the Angular build process.
 */
fun Task clean(type: Delete){
	delete "dist"
}

/**
 * Cleans everything that is created by the node plugin, i.e. the node installation and the node_modules
 * folder.
 */
fun Task cleanAll(type: Delete){
	delete "build"
	delete "node_modules"
	delete ".gradle"
}
cleanAll.dependsOn(clean)