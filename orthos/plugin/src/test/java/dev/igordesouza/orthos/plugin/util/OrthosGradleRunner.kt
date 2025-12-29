package dev.igordesouza.orthos.plugin.util

import org.gradle.testkit.runner.GradleRunner
import java.io.File

object OrthosGradleRunner {

    fun run(
        projectDir: File,
        vararg arguments: String
    ) = GradleRunner.create()
        .withProjectDir(projectDir)
        .withArguments(*arguments, "--stacktrace")
        .withPluginClasspath()
        .forwardOutput()
        .build()
}
