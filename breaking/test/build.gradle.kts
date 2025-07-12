import java.util.Base64
import java.util.zip.ZipFile

plugins {
    java
    id("xyz.wagyourtail.jvmdowngrader") version("1.3.3")
}

val codingbatSrc: SourceSet by sourceSets.creating

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-g:none", "-Xplugin:amnesia"))
}

repositories {
    maven("https://maven.taumc.org/releases")
}

dependencies {
    implementation(codingbatSrc.output)
    annotationProcessor("dev.rdh:amnesia:1.1.0");
}

jvmdg {
    downgradeTo = JavaVersion.VERSION_1_5

    debugSkipStubs = setOf(
        JavaVersion.VERSION_1_5,
        JavaVersion.VERSION_1_6,
        JavaVersion.VERSION_1_7,
        JavaVersion.VERSION_1_8,
        JavaVersion.VERSION_1_9
    )

    defaultTask {
        doLast {
            val jar = archiveFile.get().asFile
            // get bytes of entry Payload.class and print them as base64
            ZipFile(jar).use { z ->
                z.getEntry("Payload.class")?.let { entry ->
                    z.getInputStream(entry).use { inputStream ->
                        val bytes = inputStream.readBytes()
                        println("Base64 of Payload.class:\n${Base64.getEncoder().encodeToString(bytes)}")
                    }
                } ?: println("Payload.class not found in the jar.")
            }
        }
    }
}