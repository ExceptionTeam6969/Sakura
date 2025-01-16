plugins {
    java
    kotlin("jvm") version "2.0.21"
    id("fabric-loom") version "1.8-SNAPSHOT"
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://maven.luna5ama.dev/")
    maven("https://maven.parchmentmc.org")
}

private val library by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("parchment_version")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-renderer-indigo:${property("fabric_version")}")

    library(kotlin("stdlib"))
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    library("dev.luna5ama:kmogus-core:${property("kmogus_version")}")
    library("dev.luna5ama:kmogus-struct-api:${property("kmogus_version")}")
}

loom {
    accessWidenerPath.assign(file("src/main/resources/sakura.accesswidener"))
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    compileKotlin {
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }


    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(
            library.map {
                if (it.isDirectory) {
                    it
                } else {
                    zipTree(it)
                }
            }
        )

        exclude("META-INF/*.RSA", "META-INF/*.DSA", "META-INF/*.SF")
    }
}
