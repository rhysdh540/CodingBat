plugins {
    java
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

tasks.compileJava {
    options.compilerArgs.add("-Xlint:deprecation")
}