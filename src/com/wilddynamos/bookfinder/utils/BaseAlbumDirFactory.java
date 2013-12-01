package com.wilddynamos.bookfinder.utils;

import java.io.File;

import android.os.Environment;

/**
 *  Class for album storage 
 **/
public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	/**
	 *  Standard storage location for digital camera files 
	 **/
	private static final String CAMERA_DIR = "/dcim/";

	/**
	 *  get standard storage location for album 
	 **/
	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File(Environment.getExternalStorageDirectory() + CAMERA_DIR
				+ albumName);
	}
}
