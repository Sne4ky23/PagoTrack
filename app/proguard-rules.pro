# ProGuard rules for PagoTrack

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep Room entities
-keep class com.pagotrack.app.data.** { *; }

# Keep DAO interfaces
-keepclasseswithmembernames class * {
    @androidx.room.* <methods>;
}

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

# Keep Kotlin metadata
-keepclassmembers class ** {
    *** Companion;
}

# Keep coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Compose
-keep class androidx.compose.** { *; }
