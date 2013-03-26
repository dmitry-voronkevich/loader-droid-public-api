package org.zloy.android.downloader.api;

import org.zloy.android.downloader.data.AllowedConnection;
import org.zloy.android.downloader.data.Directory;

/**
 * Request to Loader Droid
 * 
 * @author Dmitry Voronkevich
 * 
 * @see #setName(String)
 * @see #setAllowedConnection(AllowedConnection)
 * @see #setDirectory(Directory)
 * @see #setCookies(String)
 * @see #setReferer(String)
 *
 */
public class LoadingRequest {
	private String link;
	private String cookies;
	private String name;
	private AllowedConnection allowedConnection = AllowedConnection.ASK_USER;
	private Directory directory;
	private String referer;

	/**
	 * Creates request to load file refered by link
	 * 
	 * @param link
	 */
	public LoadingRequest(String link) {
		this.link = link;
	}
	
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAllowedConnection(AllowedConnection allowedConnection) {
		if (allowedConnection == null)
			this.allowedConnection = AllowedConnection.ASK_USER;
		else
			this.allowedConnection = allowedConnection;
	}
	
	public void setDirectory(Directory directory) {
		if (directory == null)
			this.directory = Directory.ASK_USER_FOR_DIRECTORY;
		else
			this.directory = directory;
	}
	
	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	public String getLink() {
		return link;
	}
	
	public String getName() {
		return name;
	}
	
	public AllowedConnection getAllowedConnection() {
		return allowedConnection;
	}
	
	public Directory getDirectory() {
		return directory;
	}
	
	public String getCookies() {
		return cookies;
	}
	
	public String getReferer() {
		return referer;
	}
}
