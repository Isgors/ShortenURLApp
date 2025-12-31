package dev.igordesouza.orthos.plugin.unit

import dev.igordesouza.orthos.plugin.extension.OrthosPluginExtension
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrthosPluginExtensionTest {

    @Test
    fun `default extension values are correct`() {
        val project = ProjectBuilder.builder().build()
        project.extensions.create(
            "orthos",
            OrthosPluginExtension::class.java
        )

        val extension = project.extensions
            .getByType(OrthosPluginExtension::class.java)

        assertTrue(extension.enabled)
        assertEquals(setOf("release"), extension.enabledBuildTypes)
    }
}
