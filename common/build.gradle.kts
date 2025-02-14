import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    java
    idea
    kotlin("jvm") version "2.0.21"
    id("fabric-loom") version "1.9-SNAPSHOT"
}

group = "dev.exceptionteam"
version = "${property("mod_version")}"

base {
    archivesName = "sakura-common"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.parchmentmc.org/")
}

private val library by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = true
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("parchment_minecraft")}:${property("parchment_version")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    library("org.jetbrains.kotlin:kotlin-stdlib")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    library("dev.luna5ama:kmogus-core:${property("kmogus_version")}")
    library("dev.luna5ama:kmogus-struct-api:${property("kmogus_version")}")
}

kotlin {
    jvmToolchain(21)
}

sourceSets {
    main {
        compileClasspath += library
    }
}

tasks.compileKotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        apiVersion = KotlinVersion.KOTLIN_2_0
        languageVersion = KotlinVersion.KOTLIN_2_0
    }
}

loom {
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName = "sakura.refmap.json"
        useLegacyMixinAp = true
    }

    accessWidenerPath = file("src/main/resources/sakura.accesswidener")

    mods {
        val main by creating {
            sourceSet("main")
        }
    }
}

artifacts {
    add("library", tasks.jar.get().archiveFile) {
        builtBy(tasks.jar)
    }
}