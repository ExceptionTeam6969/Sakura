import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm")
    id("multiloader-loader")
    id("fabric-loom")
    id("com.gradleup.shadow")
}

repositories {
    mavenCentral()
}

loom {
    val aw = project(":common").file("src/main/resources/${property("mod_id")}.accesswidener")
    if (aw.exists()) {
        accessWidenerPath.set(aw)
    }
    mixin {
        defaultRefmapName.set("${property("mod_id")}.refmap.json")
    }
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

tasks.compileKotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        apiVersion = KotlinVersion.KOTLIN_2_0
        languageVersion = KotlinVersion.KOTLIN_2_0
        optIn = listOf("kotlin.RequiresOptIn", "kotlin.contracts.ExperimentalContracts")
        freeCompilerArgs = listOf(
            "-Xjvm-default=all-compatibility",
        )
    }
}

val modLibrary by configurations.creating

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("parchment_minecraft")}:${property("parchment_version")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modLibrary(project(path = ":common", configuration = "library")) {
        exclude("org.apache.commons", "commons-lang3")
        exclude("org.slf4j", "slf4j-api")
    }
}

sourceSets {
    main {
        resources { srcDir("src/generated/resources") }
        compileClasspath += modLibrary
        runtimeClasspath += modLibrary
    }
}

tasks.shadowJar {
    archiveClassifier = "named"
    configurations = listOf(modLibrary)
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    inputFile = tasks.shadowJar.get().archiveFile
}

tasks.javadocJar { enabled = false }
tasks.sourcesJar { enabled = true }
tasks.remapSourcesJar { enabled = false }