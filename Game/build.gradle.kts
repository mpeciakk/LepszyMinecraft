plugins {
    kotlin("jvm") version "1.8.10"
    application
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":Vela"))
}
