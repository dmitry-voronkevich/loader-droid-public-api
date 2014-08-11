package org.zloy.android.downloader.data;

import java.io.File;

/**
 * Abstraction whch represents directory where LD will store loading
 * 
 * Use {@link #ASK_USER_FOR_DIRECTORY} if you want LD to ask user
 * Use {@link #USE_DEFAULT_DIRECTORY} if you want LD to use default directory (usually it is /mnt/sdcard/Download, but user can override it in LDs settings)
 * Use {@link #useSpecifiedDirectory(File)} if you want specific directory
 * 
 * @author Dmitry Voronkevich
 * 
 * @since API 1
 */
public class Directory {
	public static Directory ASK_USER_FOR_DIRECTORY = new Directory();
	
	public static Directory USE_DEFAULT_DIRECTORY = new Directory();
	
	public static Directory useSpecifiedDirectory(File dir) {
		if (dir == null)
			throw new NullPointerException("Directory is null");
		return new Directory(dir);
	}

	private File mDir;
	
	public String getAbsolutePath() {
		return mDir.getAbsolutePath();
	}
	
	private Directory() {
		/* INSTANTIATION IS NOT ALLOWED */
	}

	private Directory(File dir) {
		mDir = dir;
	}
}
