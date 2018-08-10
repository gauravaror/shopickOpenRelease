# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/gaurav/android-sdks/tools/proguard/proguard-android.txt
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
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
 -keep class org.codehaus.** { *; }
 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keep public class com.acquire.shopick.** {
  public void set*(***);
  public *** get*();
}

-dontwarn com.squareup.okhttp.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.internal.huc.**
-dontwarn retrofit.appengine.UrlFetchClient
-dontwarn rx.**
-dontwarn okio.**
-keep class dagger.** { *; }
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection
-keep class javax.inject.** { *; }
-keep class java.lang.invoke.** { *; }
-keep class org.apache.http.client.** { *; }
-keep class org.apache.http.entity.** { *; }
-keep class org.apache.http.methods.** { *; }
-dontwarn dagger.internal.codegen.**
-dontwarn java.lang.invoke**

##-------------Begin Settings For Guava ------------------------------------------------------------
#https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-guava.pro
# Configuration for Guava
#
# disagrees with instructions provided by Guava project: https://code.google.com/p/guava-libraries/wiki/UsingProGuardWithGuava
#
# works if you add the following line to the Gradle dependencies
#
# provided 'javax.annotation:jsr250-api:1.0'
-keep class com.google.common.io.Resources {
    public static <methods>;
}
-keep class com.google.common.collect.Lists {
    public static ** reverse(**);
}
-keep class com.google.common.base.Charsets {
    public static <fields>;
}

-keep class com.google.common.base.Joiner {
    public static Joiner on(String);
    public ** join(...);
}

-keep class com.google.common.collect.MapMakerInternalMap$ReferenceEntry
-keep class com.google.common.cache.LocalCache$ReferenceEntry

-dontwarn sun.misc.Unsafe
-dontwarn javax.annotation.**

##-------------End Settings For Guava --------------------------------------------------------------
-dontwarn com.viewpagerindicator.**
-keep class javax.lang.model.element.Modifier
-keep class com.squareup.javawriter.JavaWriter
-keep class javax.lang.model.element.** {*;}
-dontwarn java.lang.reflect.Method
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keep class rx.internal.util.unsafe.** { *; }
-keep class com.path.android.jobqueue.**
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class com.facebook.** { *; }
-dontobfuscate
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.**
-dontwarn io.realm.**


#QuickBlox
-keep class org.jivesoftware.smack.** { *; }
-keep class com.quickblox.** { *; }
-keep class * extends org.jivesoftware.smack { public *; }
-keep class org.jivesoftware.smack.** { public *; }
-keep class org.jivesoftware.smackx.** { public *; }
-keep class com.quickblox.** { public *; }
-keep class * extends org.jivesoftware.smack { public *; }
-keep class * implements org.jivesoftware.smack.debugger.SmackDebugger { public *; }
