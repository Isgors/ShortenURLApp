package dev.igordesouza.orthos.plugin.signature

import com.android.build.api.variant.ApplicationVariant

import java.io.FileInputStream
import java.security.KeyStore
import java.security.MessageDigest

object SignatureResolver {

    fun resolve(variant: ApplicationVariant): String {
        // Access signing config via the provider API to avoid "Unresolved reference" errors
        val signing = variant.signingConfig

        val storeFile = signing.storeFile.orNull?.asFile
            ?: error("Orthos: keystore file not defined or not yet available for variant ${variant.name}")

        val storePassword = signing.storePassword.orNull
            ?: error("Orthos: keystore password missing for variant ${variant.name}")

        val keyAlias = signing.keyAlias.orNull
            ?: error("Orthos: keyAlias missing for variant ${variant.name}")

        // Note: keyPassword might be null if it's the same as storePassword
        val keyPassword = signing.keyPassword.orNull ?: storePassword

        val ks = KeyStore.getInstance(KeyStore.getDefaultType())
        FileInputStream(storeFile).use { inputStream ->
            ks.load(inputStream, storePassword.toCharArray())
        }

        val cert = ks.getCertificate(keyAlias)
            ?: error("Orthos: certificate not found for alias '$keyAlias'")

        return sha256(cert.encoded)
    }

    private fun sha256(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}