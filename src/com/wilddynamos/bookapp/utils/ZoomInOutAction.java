package com.wilddynamos.bookapp.utils;

import java.io.ByteArrayOutputStream;
import com.wilddynamos.bookapp.activity.ZoomInOutActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ZoomInOutAction {

	public static void action(Activity activity, ImageView image) {
		Drawable drawable = image.getDrawable();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		Intent intent = new Intent(activity, ZoomInOutActivity.class);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

		byte[] bytes = stream.toByteArray();
		intent.putExtra("BMP", bytes);

		activity.startActivity(intent);
	}
}