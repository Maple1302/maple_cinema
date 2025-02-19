# ----------------------------------------------
# ⚡ General ProGuard Rules
# ----------------------------------------------

# Giữ lại class có annotation của Kotlin (nếu có)
-keepattributes *Annotation*

# Giữ lại tất cả các class thuộc package của ứng dụng
-keep class com.example.maplecinema.** { *; }

# Giữ lại các lớp có liên quan đến Parcelable
-keep class * implements android.os.Parcelable { *; }
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ----------------------------------------------
# 🚀 Jetpack Compose & AndroidX
# ----------------------------------------------
-keep class androidx.compose.** { *; }
-keep class androidx.activity.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }

# Giữ lại ViewModel (nếu sử dụng)
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# ----------------------------------------------
# 🔥 Hilt & Dagger
# ----------------------------------------------
-keep class dagger.** { *; }
-keep class com.google.dagger.** { *; }
-keep class com.example.maplecinema.di.** { *; }

# Giữ lại các class được Hilt tạo ra
-keep class **_HiltModules { *; }

# ----------------------------------------------
# 📡 Retrofit & OkHttp
# ----------------------------------------------
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.moshi.** { *; }
-keep class kotlinx.serialization.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# ----------------------------------------------
# 🎥 ExoPlayer (Video Player)
# ----------------------------------------------
-keep class androidx.media3.** { *; }
-keep class com.google.android.exoplayer2.** { *; }
-keep class com.google.android.exoplayer2.ui.** { *; }

# ----------------------------------------------
# 🌐 Coil (Image Loading)
# ----------------------------------------------
-keep class coil.** { *; }
-keep class coil.compose.** { *; }
-keep class coil.memory.** { *; }
-keep class coil.request.** { *; }

# ----------------------------------------------
# 📂 Room Database (ORM)
# ----------------------------------------------
-keep class androidx.room.** { *; }
-keep class androidx.sqlite.** { *; }
-keep class com.example.maplecinema.database.** { *; }

# ----------------------------------------------
# 📄 Paging (Nếu dùng Paging Library)
# ----------------------------------------------
-keep class androidx.paging.** { *; }

# ----------------------------------------------
# 🔥 Accompanist System UI Controller
# ----------------------------------------------
-keep class com.google.accompanist.systemuicontroller.** { *; }

# ----------------------------------------------
# ⚡ ConstraintLayout for Compose
# ----------------------------------------------
-keep class androidx.constraintlayout.compose.** { *; }

# Keep GraalVM native image annotations
-keep class com.oracle.svm.** { *; }
-keep class org.graalvm.** { *; }

# Keep OkHttp internal classes
-keep class okhttp3.internal.graal.** { *; }

# Prevent stripping of reflection-based access
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations

# Preserve classes used by OkHttp
-dontwarn okhttp3.internal.graal.**
-dontwarn com.oracle.svm.**
-dontwarn org.graalvm.**

