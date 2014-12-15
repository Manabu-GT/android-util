# android-util

Utility classes for Android

## Available Utility Classes

* `AnimUtil`
* `LogUtil`
* `AppUtil`
* `CameraUtil`
* `FileUtil`
* `NetUtil`
* `StreamUtil`
* `StringUtil`
* `ToastMaster`
* `UIUtil`

###Maven

```groovy
repositories {
    maven { url 'https://raw.githubusercontent.com/Manabu-GT/mvn-repo/master/' }
}
```

###Gradle

format: aar

```groovy
dependencies {
    compile 'com.ms.square:android-util:0.1.2'
}
```

###Proguard (Sample)

```
-keep class com.ms.square.android.util.** { *; }
# the following strips off verbose and debug logs
-assumenosideeffects class com.ms.square.android.util.LogUtil {
    public static *** LOGV(...);
    public static *** LOGD(...);
}
```

## License

```
 Copyright 2014 Manabu Shimobe

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```