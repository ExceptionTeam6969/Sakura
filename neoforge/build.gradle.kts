import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm")
    id("multiloader-loader")
    id("net.neoforged.moddev")
    id("com.gradleup.shadow")
}

repositories {
    mavenLocal()
    mavenCentral()
}

tasks.javadoc {
    enabled = false
}

val modLibrary by configurations.creating

dependencies {
    modLibrary(project(path = ":common", configuration = "library")) {
        exclude("org.apache.commons", "commons-lang3")
        exclude("org.slf4j", "slf4j-api")
        exclude("com.google.code.findbugs", "jsr305")
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
        compileClasspath += modLibrary
        runtimeClasspath += modLibrary
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.shadowJar {
    configurations = listOf(modLibrary)
}

tasks.build {
    finalizedBy(tasks.shadowJar)
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
    // Automatically enable neoforge AccessTransformers if the file exists
    val at = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    parchment {
        minecraftVersion = "${property("parchment_minecraft")}"
        mappingsVersion = "${property("parchment_version")}"
    }
    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id")!!.toString())
            ideName = "NeoForge ${name.capitalize()} (${project.path})" // Unify the run config names with fabric
            additionalRuntimeClasspathConfiguration.extendsFrom(modLibrary)
        }
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

tasks.shadowJar {
    configurations = listOf(modLibrary)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.javadocJar { enabled = false }
tasks.sourcesJar { enabled = false }