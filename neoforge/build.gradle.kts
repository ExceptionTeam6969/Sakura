import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    java
    kotlin("jvm")
    id("net.neoforged.moddev")
    id("com.gradleup.shadow")
}

group = "dev.exceptionteam"
version = "${property("mod_version")}"

repositories {
    mavenLocal()
    mavenCentral()
}

tasks.javadoc {
    enabled = false
}

dependencies {
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }
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

neoForge {
    version = "${property("neoforge_version")}"

    val at = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) accessTransformers.from(at.absolutePath)

    parchment {
        minecraftVersion = "${property("parchment_minecraft")}"
        mappingsVersion = "${property("parchment_version")}"
    }

    runs {
        create("client") {
            client()
        }
    }

    mods {
        create("${property("mod_id")}") {
            sourceSet(sourceSets["main"])
        }
    }
}

tasks.sourcesJar { enabled = false }