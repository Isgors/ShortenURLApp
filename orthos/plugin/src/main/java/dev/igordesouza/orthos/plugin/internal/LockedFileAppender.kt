package dev.igordesouza.orthos.plugin.internal

import java.io.RandomAccessFile
import java.nio.ByteBuffer

/**
 * Thread-safe and process-safe file appender.
 *
 * Uses FileChannel.lock() to ensure atomic writes
 * across Gradle workers and classloader boundaries.
 */
object LockedFileAppender {

    fun append(file: java.io.File, line: String) {
        file.parentFile.mkdirs()

        RandomAccessFile(file, "rw").channel.use { channel ->
            channel.lock().use {
                channel.position(channel.size())
                channel.write(ByteBuffer.wrap(line.toByteArray()))
                channel.force(true)
            }
        }
//        RandomAccessFile(file, "rw").use { raf ->
//            val channel: FileChannel = raf.channel
//
//            channel.lock().use {
//                raf.seek(raf.length())
//                raf.writeBytes(line)
//                raf.writeBytes("\n")
//            }
//        }
    }
}
