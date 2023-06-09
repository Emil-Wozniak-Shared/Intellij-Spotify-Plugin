plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "pl.ejdev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.5")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))

    val kotest_version: String by project
    val arrow_version: String by project

    dependencies {
        api(project(":api"))
        implementation("se.michaelthelin.spotify:spotify-web-api-java:8.0.0")
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("io.arrow-kt:arrow-core:$arrow_version")
        implementation("io.arrow-kt:arrow-fx-coroutines:$arrow_version")

        testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
        testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
        testImplementation("io.kotest:kotest-assertions-core-jvm:$kotest_version")
        testImplementation("io.kotest:kotest-framework-engine-jvm:$kotest_version")

        // Logging API
        implementation("ch.qos.logback:logback-core:1.4.7")
        implementation("ch.qos.logback:logback-classic:1.4.7")

    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

