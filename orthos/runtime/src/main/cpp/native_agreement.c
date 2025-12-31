#include <jni.h>

/**
 * Returns a hardcoded agreement value.
 *
 * If the native library is replaced, recompiled,
 * hooked or proxied, this value is very likely
 * to diverge from the Java-side expectation.
 */
JNIEXPORT jint JNICALL
Java_dev_orthos_runtime_native_NativeAgreement_nativeValue(
        JNIEnv* env,
        jobject thiz
) {
    return 0x51A7C0DE;
}
