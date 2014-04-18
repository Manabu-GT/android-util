# android-util

Utility classes for Android

## Available Utility Classes

* `AnimUtil`
* `AppLog`
* `AppUtil`
* `CameraUtil`
* `FileUtil`
* `NetUtil`
* `StreamUtil`
* `StringUtil`
* `ToastMaster`
* `UIUtil`

###Maven

```
repositories {
    maven { url 'http://Manabu-GT.github.com/android-util/mvn-repo' }
}
```

###Gradle

format: aar

```
dependencies {
    compile 'com.ms.square:android-util:0.1.1'
}
```

###Proguard (Sample)

```
-keep class com.ms.square.android.util.** { *; }
# the following strips off verbose and debug logs
-assumenosideeffects class com.ms.square.android.util.AppLog {
    public static *** v(...);
    public static *** d(...);
}
```