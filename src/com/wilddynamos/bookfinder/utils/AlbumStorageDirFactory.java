package com.wilddynamos.bookfinder.utils;

import java.io.File;

/**
 *  Abstract class for album storage 
 **/
public abstract class AlbumStorageDirFactory {
	/** 
	 * get standard storage location for album 
	 **/
	public abstract File getAlbumStorageDir(String albumName);
}
