package dev.igordesouza.orthos.runtime.signal.impl

/**
 * Thin JNI bridge for native agreement validation.
 *
 * This class must not contain logic.
 */
internal object NativeAgreement {

    init {
        System.loadLibrary("orthos")
    }

    external fun nativeValue(): Long
}
