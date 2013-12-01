package com.wilddynamos.bookfinder.utils;

import java.lang.ref.WeakReference;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

/**
 *  API for decode bitmap 
 **/
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private String mCurrentPhotoPath;
	private ImageView imageView;
	private Bitmap mybitmap;

	/**
	 *  Use a WeakReference to ensure the ImageView can be garbage collected 
	 **/
	public BitmapWorkerTask(String mCurrentPhotoPath, ImageView imageView) {

		imageViewReference = new WeakReference<ImageView>(imageView);
		this.mCurrentPhotoPath = mCurrentPhotoPath;
		this.imageView = imageView;
	}

	/**
	 *  Decode image in background
	 **/
	@Override
	protected Bitmap doInBackground(Integer... params) {
		return decodeSampledBitmapFromResource(mCurrentPhotoPath, imageView);
	}

	/**
	 * Once complete, see if ImageView is still around and set bitmap
	 **/
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(View.VISIBLE);
				mybitmap = bitmap;
			}
		}
	}

	/**
	 *  decode bitmap 
	 **/
	public static Bitmap decodeSampledBitmapFromResource(
			String mCurrentPhotoPath, ImageView imageView) {

		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();

		// Get the size of the image
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Figure out which way needs to be reduced less
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		// Set bitmap options to scale the image decode target
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		// Decode the JPEG file into a Bitmap
		return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	}

	/** 
	 * get bitmap
	 **/
	public Bitmap getBitmap() {
		return mybitmap;
	}
}
