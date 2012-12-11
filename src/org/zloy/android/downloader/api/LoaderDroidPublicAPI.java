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
	
	// First version which supports open api
	private static final int MINIMUM_VERSION = 44;
	
	public static int API_VERSION = 1; 
	
	/**
	 * Checks whether LoaderDroid is installed on device
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isLoaderDroidInstalled(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		if (packageManager == null) // sometimes this can happen on strange devices
			return false;
		List<PackageInfo> packages = packageManager.getInstalledPackages(0);
		if (packages == null)
			return false;
		for (PackageInfo info: packages) {
			if (info != null && LOADER_DROID_PACKAGE.equals(info.packageName) && info.versionCode >= MINIMUM_VERSION)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if Loader Droid is installed but should be updated
	 * 
	 * @param ctx
	 * @return true if LoaderDroid need update
	 */
	public static boolean isLoaderDroidRequireUpdate(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		if (packageManager == null) // sometimes this can happen on strange devices
			return false;
		List<PackageInfo> packages = packageManager.getInstalledPackages(0);
		if (packages == null)
			return false;
		for (PackageInfo info: packages) {
			if (info != null && LOADER_DROID_PACKAGE.equals(info.packageName) && info.versionCode < MINIMUM_VERSION)
				return true;
		}
		return false;
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
		Intent intent;
		if (missingLDAction == MissingLDAction.ASK_USER_TO_INSTALL_LD)
			intent = new Intent(ctx, AddLoadingActivity.class);
		else {
			if (!isLoaderDroidInstalled(ctx))
				return false;
			intent = new Intent(ACTION_ADD_LOADING);
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
			ctx.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

}
