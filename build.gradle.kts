import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0-rc01"
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "me.pc"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.charleskorn.kaml:kaml:0.41.0")
    implementation("com.arkivanov.decompose:decompose:0.5.1")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.5.1")
    implementation("org.jasypt:jasypt:1.9.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "16"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "Holvi"
            packageVersion = "1.0.0"
        }
    }
}