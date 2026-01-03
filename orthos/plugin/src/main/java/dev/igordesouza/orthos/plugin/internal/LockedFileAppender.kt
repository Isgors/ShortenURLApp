package dev.igordesouza.orthos.plugin.internal

import java.io.RandomAccessFile
import java.nio.channels.FileChannel

/**
 * Thread-safe and process-safe file appender.
 *
 * Uses FileChannel.lock() to ensure atomic writes
 * across Gradle workers and classloader boundaries.
 */
object LockedFileAppender {

    fun appendLine(file: java.io.File, line: String) {
        file.parentFile.mkdirs()

        RandomAccessFile(file, "rw").use { raf ->
            val channel: FileChannel = raf.channel

            channel.lock().use {
                raf.seek(raf.length())
                raf.writeBytes(line)
                raf.writeBytes("\n")
            }
        }
    }
}
