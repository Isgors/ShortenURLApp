## Keep the injected security members
#-keepclassmembers class * {
#    private static final long __orthos_canary;
#    private static long __orthos_canary_value();
#    private static long __orthos_native_agreement();
#}

# Keep all public Orthos API
-keep class dev.igordesouza.orthos.runtime.** { *; }
-keep class dev.igordesouza.orthos.core.** { *; }
