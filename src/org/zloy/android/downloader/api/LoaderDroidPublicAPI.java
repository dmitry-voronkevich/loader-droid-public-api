package org.zloy.android.downloader.api;

import java.util.List;

import org.zloy.android.downloader.AddLoadingActivity;
import org.zloy.android.downloader.data.AllowedConnection;
import org.zloy.android.downloader.data.Directory;
import org.zloy.android.downloader.data.MissingLDAction;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Public API for Loader Droid download manager.
 * 
 * @author Dmitry Voronkevich
 * 
 * @since API 1
 */
public class LoaderDroidPublicAPI {
	public static final String ACTION_ADD_LOADING = "org.zloy.android.downloader.action.ADD_LOADING";
	public static final String EXTRA_NAME = "name";
	public static final String EXTRA_ALLOWED_CONNECTION = "allowed_connection";
	public static final String EXTRA_DIRECTORY = "directory";
	public static final String EXTRA_API_VERSION = "api_version";
	public static final String EXTRA_ASK_FOR_DIRECTORY = "ask_for_directory";
	public static final String EXTRA_USE_DEFAULT_DIRECTORY = "use_default_directory";
	
	public static final String LOADER_DROID_PACKAGE = "org.zloy.android.downloader";
	public static final Uri LOADER_DROID_MARKET_URI = Uri.parse("https://play.google.com/store/apps/details?id="+LOADER_DROID_PACKAGE);
	public static final String ALTERNATIVE_DOWNLOAD_LINK = "http://loader-droid-public-api.googlecode.com/files/LoaderDroid-0.8.1.apk";
	
	public static final String RESULT_ACTION_DOWNLOAD_COMPLETED = "org.zloy.android.downloader.action.DOWNLOAD_COMPLETED";
	public static final String RESULT_ACTION_DOWNLOAD_FAILED = "org.zloy.android.downloader.action.DOWNLOAD_FAILED";
	public static final String RESULT_EXTRA_FILE = "file";
	
	// First version which supports open api
	public static final int VERSION_WITH_MINIMUM_SUPPORT = 44;
	public static final int VERSION_WITH_SILENT_ADD = 45;
	public static final int VERSION_WITH_OPEN_DETAILS_ACTIVITY = 47;
	
	public static int API_VERSION = 1; 
	
	
	/**
	 * Checks installed LoaderDroid on system and returns its version. Or -1 if no loader droid installed on system.
	 * 
	 * @param ctx
	 * @return -1 or version of LoaderDroid
	 */
	public static int getLoaderDroidVersion(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		if (packageManager == null) // sometimes this can happen on strange devices
			return -1;
		List<PackageInfo> packages = packageManager.getInstalledPackages(0);
		if (packages == null)
			return -1;
		for (PackageInfo info: packages) {
			if (info != null && LOADER_DROID_PACKAGE.equals(info.packageName))
				return info.versionCode;
		}
		return -1;
	}
	
	/**
	 * Checks whether LoaderDroid is installed on device
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isLoaderDroidInstalled(Context ctx) {
		return getLoaderDroidVersion(ctx) >= VERSION_WITH_MINIMUM_SUPPORT;
	}
	
	/**
	 * Checks if Loader Droid is installed but should be updated
	 * 
	 * @param ctx
	 * @return true if LoaderDroid need update
	 */
	public static boolean isLoaderDroidRequireUpdate(Context ctx) {
		return getLoaderDroidVersion(ctx) < VERSION_WITH_MINIMUM_SUPPORT;
	}
	
	/**
	 * Adds loading to LD.
	 * 
	 * LD will ask user for loading details
	 * 
	 * @param missingLDAction what should API do if no LD installed
	 * @param ctx Context
	 * @param url link to download (supported protocols: http, https, ftp)
	 * 
	 * @return false if no LD installed on system
	 */
	public static boolean addLoading(MissingLDAction missingLDAction, Context ctx, String url) {
		return addLoading(missingLDAction, ctx, url, null);
	}
	
	/**
	 * Adds loading to LD.
	 * 
	 * LD will ask user for loading details
	 * 
	 * If you want to add loading silently, use {@link #addLoadingSilently(Context, String, String)}
	 * 
	 * @param missingLDAction what should API do if no LD installed
	 * @param ctx Context
	 * @param url link to download (supported protocols: http, https, ftp)
	 * @param name name of download
	 * 
	 * @return false if no LD installed on system
	 */
	public static boolean addLoading(MissingLDAction missingLDAction, Context ctx, String url, String name) {
		return addLoading(missingLDAction, ctx, url, name, AllowedConnection.ASK_USER, Directory.ASK_USER_FOR_DIRECTORY);
	}
	
	/**
	 * Adds loading to LD without any user interaction. 
	 * 
	 * LD will use default settings for allowed connection and directory.
	 * 
	 * @param missingLDAction what should API do if no LD installed
	 * @param ctx Context
	 * @param url link to download (supported protocols: http, https, ftp)
	 * @param name name of download
	 * 
	 * @return false if no LD installed on system
	 */
	public static boolean addLoadingSilently(MissingLDAction missingLDAction, Context ctx, String url, String name) {
		return addLoading(missingLDAction, ctx, url, name, AllowedConnection.DEFAULT, Directory.USE_DEFAULT_DIRECTORY);
	}
	
	/**
	 * Adds link for loading to LD
	 * 
	 * All arguments except context and url can be null. In this case LD will ask user.
	 * 
	 * @param missingLDAction what should API do if no LD installed
	 * @param ctx Context
	 * @param url link to download (supported protocols: http, https, ftp)
	 * @param name download name
	 * @param allowedConnection
	 * @param directory directory to store file
	 * 
	 * @return false if LD is not installed on device
	 */
	public static boolean addLoading(MissingLDAction missingLDAction, Context ctx, String url, String name, AllowedConnection allowedConnection, Directory directory) {
		final int loaderVersion = getLoaderDroidVersion(ctx);
		
		Intent intent;
		boolean allowSilentAdd = false;
		// No loader droid, or it should be updated
		if (loaderVersion < VERSION_WITH_MINIMUM_SUPPORT) {
			if (missingLDAction == MissingLDAction.ASK_USER_TO_INSTALL_LD) {
				intent = new Intent(ctx, AddLoadingActivity.class);
			} else {
				return false;
			}
		} else {
			intent = new Intent(ACTION_ADD_LOADING);
			if (loaderVersion >= VERSION_WITH_SILENT_ADD) {
				allowSilentAdd = isSilentMode(name, allowedConnection, directory);
			}
		}
		
		intent.setData(Uri.parse(url));
		intent.putExtra(EXTRA_NAME, name);
		if (allowedConnection != null) {
			intent.putExtra(EXTRA_ALLOWED_CONNECTION, allowedConnection.name());
		}
		if (directory == Directory.ASK_USER_FOR_DIRECTORY) {
			intent.putExtra(EXTRA_ASK_FOR_DIRECTORY, true);
		} else if (directory == Directory.USE_DEFAULT_DIRECTORY) {
			intent.putExtra(EXTRA_USE_DEFAULT_DIRECTORY, true);
		} else if (directory != null) {
			intent.putExtra(EXTRA_DIRECTORY, directory.getAbsolutePath());
		}
		intent.putExtra(EXTRA_API_VERSION, API_VERSION); // For future releases
		try {
			if (allowSilentAdd)
				ctx.sendBroadcast(intent);
			else
				ctx.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}
	
	
	/**
	 * Every time LoaderDroid completes loading, it sends broadcast message 
	 * with org.zloy.android.downloader.action.DOWNLOAD_COMPLETED.
	 * 
	 * Intent will contain initial url as an intent data and absolute file path as string extra "file".
	 * 
	 * Use {@link #getInitialUrl(Intent)} and {@link #getDownloadedFilePath(Intent)} to extract them.
	 *  
	 * @param intent
	 * @return
	 */
	public static boolean isSuccessfullDownload(Intent intent) {
		if (intent == null)
			return false;
		return RESULT_ACTION_DOWNLOAD_COMPLETED.equals(intent.getAction());
	}

	/**
	 * Every time LoaderDroid fails loading, it sends broadcast message 
	 * with org.zloy.android.downloader.action.DOWNLOAD_FAILED.
	 * 
	 * Intent will contain initial url as an intent data.
	 * 
	 * Use {@link #getInitialUrl(Intent)} to extract it.
	 *  
	 * @param intent
	 * @return
	 */
	public static boolean isFailedDownload(Intent intent) {
		if (intent == null)
			return false;
		return RESULT_ACTION_DOWNLOAD_FAILED.equals(intent.getAction());
	}
	
	/**
	 * Extracts absolute file path from download_completed itent from LoaderDroid
	 * 
	 * @param intent
	 * @return
	 * 
	 * @see #isSuccessfullDownload(Intent)
	 */
	public static String getDownloadedFilePath(Intent intent) {
		if (intent == null)
			return null;
		return intent.getStringExtra(RESULT_EXTRA_FILE);
	}
	
	/**
	 * Returns initial url which was downloaded of failed from broadcast message from LD
	 * 
	 * @param intent
	 * @return
	 * 
	 * @see #isFailedDownload(Intent)
	 * @see #isSuccessfullDownload(Intent)
	 */
	public static String getInitialUrl(Intent intent) {
		if (intent == null)
			return null;
		return intent.getDataString();
	}
	
	private static boolean isSilentMode(String name,
			AllowedConnection allowedConnection, Directory directory) {
		if (TextUtils.isEmpty(name))
			return false;
		if (directory == null || directory == Directory.ASK_USER_FOR_DIRECTORY)
			return false;
		if (allowedConnection == null || allowedConnection == AllowedConnection.ASK_USER)
			return false;
		return true;
	}

}
