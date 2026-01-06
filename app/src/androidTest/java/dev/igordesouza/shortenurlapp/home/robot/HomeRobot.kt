package dev.igordesouza.shortenurlapp.home.robot

/**
 * Robot abstraction for Home screen interactions.
 *
 * Reads like a user story and hides UI technology details
 * (Compose / Espresso / UI Automator).
 */
interface HomeRobot {

    fun enterUrl(url: String): HomeRobot

    fun shortenUrl(): HomeRobot

    fun copyUrl(): HomeRobot

    fun assertClipboardContains(text: String) : HomeRobot
    fun assertBrowserOpened(): HomeRobot

    fun assertShortenedUrlDisplayed(): HomeRobot

    fun assertEmptyStateVisible(): HomeRobot

    fun givenSingleShortenedUrl(url: String): HomeRobot

    fun givenMultipleShortenedUrls(urls: List<String>): HomeRobot

}
