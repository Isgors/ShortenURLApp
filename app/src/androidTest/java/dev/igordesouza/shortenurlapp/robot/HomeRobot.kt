package dev.igordesouza.shortenurlapp.robot

/**
 * Robot abstraction for Home screen interactions.
 *
 * Reads like a user story and hides UI technology details
 * (Compose / Espresso / UI Automator).
 */
interface HomeRobot {

    fun waitForHomeScreen(): HomeRobot

    fun enterUrl(url: String): HomeRobot

    fun shortenUrl(): HomeRobot

    fun waitForAnyShortenedUrl(): HomeRobot

    fun waitForShortenedUrlContaining(text: String): HomeRobot

    fun openFirstUrlActions(text: String): HomeRobot

    fun openDeleteAllDialog(): HomeRobot

    fun confirmDeletion(): HomeRobot

    fun copyUrl(): HomeRobot

    fun openUrlInBrowser(): HomeRobot

    fun assertClipboardContains(text: String): HomeRobot

    fun assertBrowserOpened(): HomeRobot

    fun assertEmptyStateVisible(): HomeRobot
}
