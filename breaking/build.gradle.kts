plugins {
    java
    idea
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

idea.module.isDownloadSources = true

tasks.register<JavaExec>("runSpammer") {
    group = "run"
    mainClass = "rdh.codingbat.Spammer"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runParser") {
    group = "run"
    mainClass = "rdh.codingbat.Parser"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runMain") {
    group = "run"
    mainClass = "rdh.codingbat.Main"
    classpath = sourceSets["main"].runtimeClasspath
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.18.1")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("blue.endless:jankson:1.2.3")
}