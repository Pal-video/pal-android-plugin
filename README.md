# Pal.video
[![](https://jitpack.io/v/Pal-video/pal-android-plugin.svg)](https://jitpack.io/#Pal-video/pal-android-plugin)
**Coming soon**

Your new in app friend.

- Get feedbacks in your app using the power of videos.
- Onboard your users

## Installation 
**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
	
**Step 2.** Add the dependency
```gradle 
	dependencies {
	        implementation 'com.github.Pal-video:pal-android-plugin:1.0.0-alpha'
	}
```

**Step 3.** Setup the plugin with Api token

Setup the plugin using the develop or production apiToken (available in your web admin dashboard)
```kotlin
PalPlugin.setup(this, token)
```

**Step 4.** push your screen events to our backend 

We simply needs a context.
Second argument is a route name. (We use this to have a standard accross all frameworks including web)
```kotlin
PalPlugin.instance.logCurrentScreen(context, "/")
```

You are done 👏


--------------


## FAQ

- I have this error when I build : 

> "Duplicate class androidx.lifecycle.ViewModelLazy found in modules jetified-lifecycle-viewmodel-ktx-2.3.1-runtime (androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1) and lifecycle-viewmodel-2.5.1-runtime (androidx.lifecycle:lifecycle-viewmodel:2.5.1)"

**Solution** 
Add this dependency in your build gradle
```kotlin
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'
```



