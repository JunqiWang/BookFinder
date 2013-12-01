package com.wilddynamos.bookfinder.utils;

import java.io.File;

import android.os.Environment;

/** 
 * Class for album storage 
 **/
public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {
	/** 
	 * get standard storage location for pictures 
	 **/
	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
	}
}
