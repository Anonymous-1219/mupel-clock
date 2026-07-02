# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep ViewBinding
-keep class com.mupel.clock.databinding.** { *; }

# Keep coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep AndroidX
-keep class androidx.** { *; }

# Keep our app classes
-keep class com.mupel.clock.** { *; }

# Remove logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
