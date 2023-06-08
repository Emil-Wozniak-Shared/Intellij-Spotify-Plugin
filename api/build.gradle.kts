plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
}

group = "pl.ejdev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("se.michaelthelin.spotify:spotify-web-api-java:8.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.arrow-kt:arrow-core:1.2.0-RC")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0-RC")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}