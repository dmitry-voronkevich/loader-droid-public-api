package org.zloy.android.downloader.data;

/**
 * Abstraction for allowed connection type for loading in LD
 * 
 * @author Dmitry Voronkevich
 * 
 * @since API 1
 */
public enum AllowedConnection {
	/**
	 * Use default connection type, which user selected in LDs settings
	 */
	DEFAULT,
	
	/**
	 * Ask user for allowed connection
	 */
	ASK_USER,
	
	/**
	 * Automatic connection mode (based on file size)
	 */
	AUTO,
	
	/**
	 * Allow loading ONLY on WI-FI
	 */
	WIFI_ONLY,
	
	/**
	 * Allow loading on WI-FI and mobile internet (3G, gprs, etc)
	 */
	WIFI_AND_MOBILE,
	
	/**
	 * Allow any type of connection and allow loading in roaming
	 */
	ROAMING, 
}
