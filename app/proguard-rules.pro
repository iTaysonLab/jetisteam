# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes *Annotation*

-renamesourcefileattribute
-repackageclasses
-allowaccessmodification

# from: https://www.guardsquare.com/blog/eliminating-data-leaks-caused-by-kotlin-assertions
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
	public static void checkNotNull(...);
	public static void checkExpressionValueIsNotNull(...);
	public static void checkNotNullExpressionValue(...);
	public static void checkParameterIsNotNull(...);
	public static void checkNotNullParameter(...);
	public static void checkReturnedValueIsNotNull(...);
	public static void checkFieldIsNotNull(...);
	public static void throwUninitializedPropertyAccessException(...);
	public static void throwNpe(...);
	public static void throwJavaNpe(...);
	public static void throwAssert(...);
	public static void throwIllegalArgument(...);
	public static void throwIllegalState(...);
}

-assumenosideeffects class android.util.Log {
    public static void d(...);
    public static void v(...);
}

-dontwarn kotlin.**
-dontwarn org.slf4j.**

-keep class org.h2.Driver { *; }
-keep class org.h2.engine.Engine { *; }
-keep class org.h2.store.fs.** { *; }
-dontwarn org.h2.**
-dontnote org.h2.**