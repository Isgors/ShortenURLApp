package dev.igordesouza.orthos.runtime.util

import android.content.Context
import android.content.pm.PackageManager
import java.security.MessageDigest

/**
 * Utilities for retrieving the currently installed
 * APK signature at runtime.
 */
object SignatureUtils {

    fun currentSignature(context: Context): String {
        val pm = context.packageManager
        val pkg = context.packageName

        val info = pm.getPackageInfo(
            pkg,
            PackageManager.GET_SIGNING_CERTIFICATES
        )

        val cert = info.signingInfo?.apkContentsSigners?.first()?.toByteArray()
        return sha256(cert)
    }

    private fun sha256(bytes: ByteArray?): String {
        return bytes?.let {
            val digest = MessageDigest.getInstance("SHA-256")
                .digest(bytes)

            return digest.joinToString("") { "%02x".format(it) }
        }.run {
            ""
        }

    }
}
