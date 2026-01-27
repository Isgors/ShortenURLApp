package dev.igordesouza.shortenurlapp.category

import org.junit.experimental.categories.Category
import org.junit.runner.Description

/** Marks stable, release-gate tests */
interface ReleaseGate

/** Marks flaky or system-level tests */
interface Flaky

/** Marks system / UiAutomator tests */
interface SystemUi

/**
 * Extracts JUnit4 categories as non-null String values.
 */
fun Description.extractCategories(): List<String> =
    annotations
        .filterIsInstance<Category>()
        .flatMap { it.value.toList() }
        .mapNotNull { it.simpleName }
