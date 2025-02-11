import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    java
    kotlin("jvm") version "2.0.21"
    id("multiloader-common")
    id("net.neoforged.moddev")
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
}

neoForge {
    neoFormVersion = property("neo_form_version")!!.toString()
    // Automatically enable AccessTransformers if the file exists
    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    parchment {
        minecraftVersion = property("parchment_minecraft")!!.toString()
        mappingsVersion = property("parchment_version")!!.toString()
    }
}

private val library by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    implementation("org.spongepowered:mixin:0.8.5")
    implementation("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    library("org.jetbrains.kotlin:kotlin-stdlib")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    library("dev.luna5ama:kmogus-core:${property("kmogus_version")}")
    library("dev.luna5ama:kmogus-struct-api:${property("kmogus_version")}")
}

kotlin {
    jvmToolchain(21)
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonKotlin") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile) {
        builtBy(tasks.compileJava)
    }
    for (file in sourceSets.main.get().kotlin.sourceDirectories.files) {
        add("commonKotlin", file) {
            builtBy(tasks.compileKotlin)
        }
    }
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile) {
        builtBy(tasks.processResources)
    }
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
        optIn = listOf("kotlin.RequiresOptIn", "kotlin.contracts.ExperimentalContracts")
        freeCompilerArgs = listOf(
            "-Xjvm-default=all-compatibility",
        )
    }
}

tasks {
    jar {
        enabled = false
    }

    javadocJar {
        enabled = false
    }
}