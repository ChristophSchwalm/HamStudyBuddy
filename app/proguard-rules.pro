# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Retrofit & Moshi
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.moshi.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn com.squareup.moshi.**

# Retrofit and Moshi model classes
-keepclassmembers class ** {
    @com.squareup.moshi.Json <fields>;
}

# Prevent obfuscation of Moshi JSON model class fields
-keepnames class ** {
    @com.squareup.moshi.Json <fields>;
}