# Introduction
[Loader Droid](https://play.google.com/store/apps/details?id=org.zloy.android.downloader) is high quality download manager available for Android. Any Android application can use it's power to download files.

Here are some features supported by LoaderDroid:

* Resumable downloads (continue loading from the same place it was interrupted)
* Auto pause when connection lost
* Auto resume when connection reappear
* Intercepts links from Browser and other applications
* You can define what connection is allowed for each link (WI-FI, 3G, etc)
* Stable, fast and easy to use
* Currently supports http/https/ftp

If you want to reuse power of Loader Droid, please read next:

# Details
Download source code, by executing `git clone https://github.com/dmitry-voronkevich/loader-droid-public-api`

```sh
cd loader-droid-public-api
gradle uploadArchives
```

In your project's build.gradle make sure that you have local repositories setup
```groovy
repositories {
    mavenLocal()
}
```
And add dependency to api lib

```
dependencies {
    compile 'org.zloy.android.downloader.api:loader-droid-public-api:1.0'
}
```

Next, in your java code call to:
```java
if (!LoaderDroidPublicAPI.addLoading(MissingLDAction.RETURN_FALSE, context, myLink)) {
    doOwnLoading(myLink);
}
```

In this case library will check if LoaderDroid is installed in system and delegates loading to it. If it is not installed, it will return false.

If you don't want to write your own loading code you can call
```java
LoaderDroidPublicAPI.addLoading(MissingLDAction.ASK_USER_TO_INSTALL_LD, this, myLink);
```
But you should add some more code inside AndroidManifest.xml

```xml
<application ...>

    ...

    <activity android:name="org.zloy.android.downloader.AddLoadingActivity" android:theme="@style/AddLoadingActivityTheme.DeviceDefault"/>
  
    ...

</application>
```
This will check if LoaderDroid installed and if not - show dialog asking user to install LoaderDroid.
