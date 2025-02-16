import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm")
    id("multiloader-common")
    id("net.neoforged.moddev")
}

group = "dev.exceptionteam"
version = "${project.property("mod_version")}"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
}

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fapi_version: String by project
val reflections_version: String by project

val kmogusVersion= "1.1-SNAPSHOT"
val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else if (arch.startsWith("ppc"))
                "natives-linux-ppc64le"
            else if (arch.startsWith("riscv"))
                "natives-linux-riscv64"
            else
                "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) }     ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        arrayOf("Windows").any { name.startsWith(it) }                ->
            "natives-windows"
        else                                                                            ->
            throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
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

lateinit var library: Configuration

configurations {
    library = create("library") {
        isCanBeResolved = true
    }
}

dependencies {
    implementation("org.spongepowered:mixin:0.8.5")
    implementation("io.github.llamalad7:mixinextras-common:0.3.5")
    implementation("org.ow2.asm:asm-tree:9.6")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")

    library("org.jetbrains.kotlin:kotlin-stdlib")
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    library("dev.luna5ama:kmogus-core:$kmogusVersion")
    library("dev.luna5ama:kmogus-struct-api:$kmogusVersion")

    library("org.lwjgl:lwjgl-assimp:3.3.3") {
        exclude("org.lwjgl", "lwjgl")
    }
    library("org.lwjgl", "lwjgl-assimp", "3.3.3", classifier = lwjglNatives) {
        exclude("org.lwjgl", "lwjgl")
    }
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
        apiVersion = KotlinVersion.KOTLIN_2_2
        languageVersion = KotlinVersion.KOTLIN_2_2
        optIn = listOf("kotlin.RequiresOptIn", "kotlin.contracts.ExperimentalContracts")
        freeCompilerArgs = listOf(
            "-Xjvm-default=all-compatibility",
            "-Xcontext-receivers"
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