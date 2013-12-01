package com.wilddynamos.bookfinder.utils;

import java.io.ByteArrayOutputStream;

import com.wilddynamos.bookfinder.activity.ZoomInOutActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/** 
 * API for Zoom in and zoom out 
 **/
public class ZoomInOutAction {

	public static void action(Activity activity, ImageView image) {
		Drawable drawable = image.getDrawable();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		Intent intent = new Intent(activity, ZoomInOutActivity.class);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);

		byte[] bytes = stream.toByteArray();
		intent.putExtra("BMP", bytes);

		activity.startActivity(intent);
	}
}