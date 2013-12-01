package com.wilddynamos.bookfinder.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wilddynamos.bookfinder.R;

/**
 * API for taking photo 
 **/

public class TakePhoto {
	private static final int ACTION_TAKE_PHOTO = 1;

	private String mCurrentPhotoPath;
	private ImageView imageView;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	private Button takePhoto;

	Activity activity;

	public TakePhoto(Activity activity, String mCurrentPhotoPath,
			ImageView imageView, Button button) {
		this.activity = activity;
		this.mCurrentPhotoPath = mCurrentPhotoPath;
		this.imageView = imageView;
		this.takePhoto = button;
	}

	private String getAlbumName() {
		return activity.getString(R.string.album_name);
	}

	/** 
	 * get photo album for this application 
	 **/
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(activity.getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	/** 
	 * Create an image file 
	 **/
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	/** 
	 * set up photo file 
	 **/
	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();

		return f;
	}

	/**
	 *  show picture in gallery 
	 **/
	public void galleryAddPic(String path) {
		Intent mediaScanIntent = new Intent(
				"android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		activity.sendBroadcast(mediaScanIntent);
	}

	/** 
	 * start taking photo activity 
	 **/
	public void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File f = null;

		try {
			f = setUpPhotoFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		} catch (IOException e) {
			e.printStackTrace();
			f = null;
			mCurrentPhotoPath = null;
		}
		activity.startActivityForResult(takePictureIntent, actionCode);
	}

	/** 
	 * handle camera photo 
	 **/
	public void handleCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			BitmapWorkerTask task = new BitmapWorkerTask(mCurrentPhotoPath,
					imageView);
			task.execute();
		}
	}

	Button.OnClickListener mTakePicOnClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
		}
	};

	/** 
	 * Indicates whether the specified action can be used as an intent
	 **/
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/** 
	 * set button listener 
	 **/
	private void setBtnListenerOrDisable(Button btn,
			Button.OnClickListener onClickListener, String intentName) {
		if (isIntentAvailable(activity, intentName)) {
			btn.setOnClickListener(onClickListener);
		} else {
			btn.setText(activity.getText(R.string.cannot).toString() + " "
					+ btn.getText());
			btn.setClickable(false);
		}
	}

	/** 
	 * start taking photo action 
	 **/
	public void start() {
		// it has define take photo button above
		setBtnListenerOrDisable(takePhoto, mTakePicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE);
		// differet API use different storage dir
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}

	}

	/** 
	 * get the path of the photo 
	 **/
	public String getPath() {
		return mCurrentPhotoPath;
	}

}