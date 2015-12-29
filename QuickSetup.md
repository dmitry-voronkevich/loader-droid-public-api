# Introduction #

Loader Droid is high quality download manager.

Highlights:
  * Resumable downloads (continue loading from the same place it was interrupted)
  * Auto pause when connection lost
  * Auto resume when connection reappear
  * Intercepts links from Browser and other applications
  * You can define what connection is allowed for each link (WI-FI, 3G, etc)
  * Stable, fast and easy to use

If you want to reuse power of Loader Droid, please read next

# Details #

Download source code.
```
cd loader-droid-public-api
gradle uploadArchives
```

In your project build.gradle make sure that you have local repositories setup
```
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

Next, call to:
```
if (!LoaderDroidPublicAPI.addLoading(MissingLDAction.RETURN_FALSE, this, myLink)) {
    doOwnLoading(myLink);
}
```
In this case library will check if LoaderDroid is installed in system and delegates loading to it. If it is not installed, it will return false.

If you don't want to write your own loading code you can call
```
LoaderDroidPublicAPI.addLoading(MissingLDAction.ASK_USER_TO_INSTALL_LD, this, myLink);
```

But you should add some more code inside AndroidManifest.xml
```
<application ...>

    ...

    <activity android:name="org.zloy.android.downloader.AddLoadingActivity" android:theme="@style/AddLoadingActivityTheme.DeviceDefault"/>
  
    ...

</application>
```

This will check if LoaderDroid installed and if not - show dialog asking user to install LoaderDroid.