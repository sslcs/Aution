# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/cs/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.happy.auction.R$* {
    public static final int *;
}
-keep class com.happy.auction.entity.** {*;}

-keepattributes *Annotation*

#ShareSDk
-keep class com.mob.tools.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn com.mob.tools.**
-dontwarn **.R$*

#Jpush
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** {*;}

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** {*;}

#保留行号
-keepattributes SourceFile,LineNumberTable

##自定义View不能被混淆
-keep public class * extends android.view.* {
    void set*(***);
    *** get*();
}
-keep public class * extends android.widget.* {
    void set*(***);
    *** get*();
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * implements TypeEvaluator<*> {*;}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#WheelPicker
-keep class com.aigestudio.wheelpicker.** {*;}

#rxjava
-dontwarn sun.misc.**
-keep class sun.misc.** {*;}
-dontwarn javax.annotation.**
-keep class javax.annotation.** {*;}

#glide
-keep class com.bumptech.glide.** {*;}
#picasso
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** {*;}
-dontwarn okio.**
-keep class okio.** {*;}

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose