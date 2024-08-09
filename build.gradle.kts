plugins {
    java
    kotlin("jvm") version "2.0.0"
    id("fabric-loom") version "1.7-SNAPSHOT"
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://maven.luna5ama.dev/")
}

val library by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-renderer-indigo:${property("fabric_version")}")

    library(kotlin("stdlib"))
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    library("dev.luna5ama:kmogus-core:${property("kmogus_version")}")
    library("dev.luna5ama:kmogus-struct-api:${property("kmogus_version")}")
}

loom {
    accessWidenerPath.assign(file("src/main/resources/sakura.accesswidener"))
}

kotlin {
    jvmToolchain(21)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    compileKotlin {
        @Suppress("DEPRECATION")
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.contracts.ExperimentalContracts",
                "-Xlambdas=indy",
                "-Xjvm-default=all",
                "-Xbackend-threads=0",
                "-Xno-source-debug-extension"
            )
        }
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

    fun registerAutoBuildTask(name: String, targetFolder: String) {
        register<Copy>(name) {
            group = "auto build"
            dependsOn("build")

            if (file("$targetFolder/Sakura*.jar").exists()) {
                delete {
                    fileTree("$targetFolder/").matching {
                        include("Sakura*.jar")
                    }
                }
            }

            from("build/libs/")
            include("Sakura*.jar")
            into("$targetFolder/")
        }
    }
}