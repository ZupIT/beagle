#
# Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

-dontwarn org.jetbrains.annotations.**
-dontwarn javax.annotation.**

# Beagle uses coroutines in network requests
-keep class kotlinx.coroutines.experimental.android.AndroidExceptionPreHandler { *; }

# Beagle does reflection on generic parameters
-keepattributes Signature, InnerClasses, EnclosingMethod

# Beagle does reflection on method and parameter annotations
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Yoga is a dependency used on Beagle
-keep @com.facebook.proguard.annotations.DoNotStrip class * { *; }
-keep class com.facebook.soloader.{*;}
-keep class com.facebook.yoga.{*;}
-keep class com.facebook.jni.**{*;}
-keep class com.facebook.fbjni.**{*;}

# Beagle Serialization / Deserialization
-keep class br.com.zup.beagle.core.BeagleJson

-keep @br.com.zup.beagle.annotation.RegisterWidget class *  {
  <init>(...);
  <fields>;
}

-keep @br.com.zup.beagle.annotation.RegisterAction class *  {
  <init>(...);
  <fields>;
}

-keep @br.com.zup.beagle.core.BeagleJson class *  {
  <init>(...);
  <fields>;
  **[] values();
}

-keep class ** {
  @br.com.zup.beagle.android.annotation.ContextDataValue *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes LineNumberTable,SourceFile
#-dontobfuscate


