package com.wilddynamos.bookfinder.activity.profile;

import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.activity.BaseProfileActivity;
import com.wilddynamos.bookfinder.model.User;
import com.wilddynamos.bookfinder.ws.local.GetMyProfile;
import com.wilddynamos.bookfinder.ws.remote.action.Logout;

public class MyProfileActivity extends BaseProfileActivity {

	@Override
	protected void createFunctionSpecificView() {
		((LinearLayout) findViewById(R.id.profile_self))
				.setVisibility(LinearLayout.VISIBLE);

		new GetMyProfile(this).execute();
	}

	public void fill(User user) {
		name.setText(user.getName());
		email.setText(user.getEmail());
		gender.setText(user.getGender() ? "Male" : "Female");
		campus.setText(user.getCampus());
		contact.setText(user.getContact());
		address.setText(user.getAddress());

		String photoPath = user.getPhotoAddr();
		if (photoPath != null) {
			try {
				Bitmap bmp = BitmapFactory.decodeFile(photoPath);
				profileImage.setImageBitmap(bmp);
			} catch (Exception e) {
			}
		}
		bg.setAlpha(0f);
	}

	public void editProfile(View view) {
		Intent intent = new Intent(this, EditProfileActivity.class);

		intent.putExtra("name", name.getText().toString());
		intent.putExtra("gender", "Male".equals(gender.getText().toString()));
		intent.putExtra("campus", campus.getText().toString());
		intent.putExtra("contact", contact.getText().toString());
		intent.putExtra("address", address.getText().toString());

		Drawable drawable = profileImage.getDrawable();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
		byte[] bytes = stream.toByteArray();
		intent.putExtra("BMP", bytes);

		startActivity(intent);
	}

	public void logOut(View view) {
		new AlertDialog.Builder(this)
				.setTitle("Log Out?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								logOutAction();
							}
						}).setNegativeButton("Back", null).show();
	}

	public void logOutAction() {
		new Logout(this).execute();
	}

	public void changePassword(View view) {
		Intent intent = new Intent(this, ChangePasswordActivity.class);
		startActivity(intent);
	}
}
