plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm") version "2.2.21"
    `java-gradle-plugin`
}

group = "dev.igordesouza.orthos"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
    compileOnly("com.android.tools.build:gradle:8.12.1")
    implementation(kotlin("stdlib"))
    testImplementation(libs.junit)
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
    // Android Gradle Plugin API
    implementation("com.android.tools.build:gradle-api:8.12.1")
}

kotlin {
    jvmToolchain(17)
}
