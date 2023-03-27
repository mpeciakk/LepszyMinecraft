plugins {
    kotlin("jvm") version "1.8.10"
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

val lwjglVersion = "3.3.1"

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "FreeBSD", "SunOS", "Unit").any { name.startsWith(it) } ->
            "natives-linux"

        arrayOf("Windows").any { name.startsWith(it) } ->
            "natives-windows"

        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

dependencies {
    api(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    api("org.joml:joml:1.10.6-SNAPSHOT")
    api("org.lwjgl", "lwjgl")
    api("org.lwjgl", "lwjgl-glfw")
    api("org.lwjgl", "lwjgl-opengl")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
}
