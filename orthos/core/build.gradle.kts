plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm") version "2.2.21"

}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}
