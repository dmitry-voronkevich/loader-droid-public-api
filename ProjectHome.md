LoaderDroid is a high quality download manager for android (https://play.google.com/store/apps/details?id=org.zloy.android.downloader). It's power could be reused by any other android application via public API.

• Can be used as a downloader for media/any other files
• Supports silent download

Currently supports http/https/ftp

Any android application can delegate any loading to LoaderDroid. It will take care of it by queuing, storing, loading.

Code usage is very simple:
```
LoaderDroidPublicAPI.addLoading(MissingLDAction.ASK_USER_TO_INSTALL_LD, this, myLink);
```
Easy peasy.