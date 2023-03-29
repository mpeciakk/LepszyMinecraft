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
    api(project(":Ain"))

    api("io.github.spair:imgui-java-binding:1.86.10")
    api("io.github.spair:imgui-java-lwjgl3:1.86.10")
    runtimeOnly("io.github.spair:imgui-java-$lwjglNatives:1.86.10")
}

