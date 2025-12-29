package dev.igordesouza.orthos.plugin.canary

import java.security.SecureRandom

/**
 * Gera seeds criptograficamente fortes para o canary.
 *
 * O seed é combinado via XOR com valores estáticos
 * para evitar fingerprinting direto.
 */
object CanarySeedGenerator {

    private val random = SecureRandom()

    fun generateSeed(): Long {
        return random.nextLong()
    }
}
