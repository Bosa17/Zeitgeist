# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting com build.gradle.
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
##---------------Begin: proguard configuration for RenderScript  ----------
-keepclasseswithmembernames class * {
native <methods>;
}
-keep class androidx.renderscript.** { *; }
##---------------End: proguard configuration for RenderScript  ----------
##---------------Begin: proguard configuration for Paytm  ----------
-keepclassmembers class com.paytm.pgsdk.paytmWebView$PaytmJavaScriptInterface {
   public *;
}
##---------------End: proguard configuration for Paytm  ----------