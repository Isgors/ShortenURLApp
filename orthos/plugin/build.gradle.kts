plugins {
    `java-library`
    kotlin("jvm")
    `java-gradle-plugin`
}

group = "dev.igordesouza.orthos"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

gradlePlugin {
    plugins {
        create("orthosRuntime") {
            id = "dev.igordesouza.orthos.runtime"
            implementationClass =
                "dev.igordesouza.orthos.plugin.OrthosRuntimePlugin"
        }
    }
}

dependencies {
    implementation("org.ow2.asm:asm:9.9.1")
    implementation("org.ow2.asm:asm-commons:9.9.1")
    implementation("org.ow2.asm:asm-util:9.9.1")
    testImplementation(libs.junit)
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
    // Android Gradle Plugin API
    implementation("com.android.tools.build:gradle-api:8.5.2")
}

kotlin {
    jvmToolchain(11)
}
