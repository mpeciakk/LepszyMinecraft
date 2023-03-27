plugins {
    kotlin("jvm") version "1.8.10"
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":Ain"))
    api(project(":Aries"))
    api(project(":Librae"))
}

