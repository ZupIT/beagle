# Beagle uses coroutines in network requests
-keep class kotlinx.coroutines.experimental.android.AndroidExceptionPreHandler { *; }

## Beagle does reflection on generic parameters
#-keepattributes Signature, InnerClasses, EnclosingMethod
#
## Beagle does reflection on method and parameter annotations
#-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
#-dontwarn org.jetbrains.annotations.**
#-keep class kotlin.Metadata { *; }
#
## Yoga is a dependency used on Beagle
-keep @com.facebook.proguard.annotations.DoNotStrip class * { *; }



## Customized classes for Beagle
#-keep @br.com.zup.beagle.annotation.** class * { *; }
#-keep @br.com.zup.beagle.android.annotation.** class * { *; }
#-keep class * extends br.com.zup.beagle.android.widget.**
#
## Core classes in Beagle
#-keep class br.com.zup.beagle.android.action.** { *; }
#-keep class br.com.zup.beagle.android.widget.** { *; }
#-keep class br.com.zup.beagle.android.components.** { *; }
#-keep class br.com.zup.beagle.android.context.** { *; }
#-keep class br.com.zup.beagle.widget.** { *; }
#-keep class br.com.zup.beagle.core.** { *; }
#-keep class br.com.zup.beagle.analytics.** { *; }
#




# Do not strip any method/class that is annotated with @DoNotStrip
-keep @br.com.zup.beagle.annotation.DoNotStrip class *
-keepclassmembers class * {
    @br.com.zup.beagle.annotation.DoNotStrip *;
}

-keepclassmembers @br.com.zup.beagle.annotation.DoNotStrip class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers @br.com.zup.beagle.annotation.RegisterWidget class * {
    <init>(...);
}