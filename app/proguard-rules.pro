# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/shixionglu/Library/Android/tools/proguard/proguard-android.txt
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


-optimizationpasses 7  #指定代码的压缩级别 0 - 7
-repackageclasses ''  # //把执行后的类重新放在某一个目录下，后跟一个目录名
-allowaccessmodification   #优化时允许访问并修改有修饰符的类和类的成员
-keepattributes Signature  #gson TypeToken 问题解决方法   不混淆泛型
-keepattributes *Annotation*  #保护给定的可选属性，例如   假如项目中有用到注解，应加入这行配置

#-applymapping mapping.txt



-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.cloudmachine.R$*{
public static final int *;
}

-keep class com.cloudmachine.struc.**{
    *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** e(...);
}



-keep public class * implements java.io.Serializable {*;}


#-libraryjars libs/AMap_2DMap_V2.3.0.jar
-dontwarn com.**
-keep class assets.** { *; }


#-libraryjars libs/uk_co_senab_photoview.jar
-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.** { *; }


#-libraryjars libs/httpmime-4.1.2.jar
-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.** { *; }



#-libraryjars libs/universal-image-loader-1.9.0.jar
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }


#-libraryjars libs/download-manager.jar
-dontwarn com.whl.helper.**
-keep class com.whl.helper.** { *; }

#-libraryjars  libs/android-support-v4.jar
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment


#-libraryjars libs/jpush-sdk-release1.7.3.jar
-dontwarn cn.jpush.android.**
-keep class assets.** { *; }
-keep class cn.jpush.android.** { *; }

#-libraryjars libs/gson-2.3.1.jar
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.annotations.** { *; }
-keep class com.google.gson.internal.** { *; }
-keep class com.google.gson.internal.bind.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.reflect.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class org.json.** {*;}

#-libraryjars libs/pinyin4j-2.5.0.jar
-dontwarn com.hp.hpl.sparta.**
-dontwarn demo.**
-dontwarn net.sourceforge.pinyin4j.**
-keep class com.hp.hpl.sparta.** { *; }
-keep class demo.** { *; }
-keep class net.sourceforge.pinyin4j.** { *; }


-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.alipay.euler.andfix.**{
    *;
}
-keep class com.taobao.hotfix.aidl.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-keep class com.taobao.hotfix.HotFixManager{
    public *;
}

-keep class com.tencent.mm.sdk.** {
    *;
 }


#-libraryjars libs/alipaySingle-20161222.jar

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-keep class com.jsbridge.**{*;}






-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod