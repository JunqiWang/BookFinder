package com.wilddynamos.bookapp.activity.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.LinearLayout;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.BaseProfileActivity;
import com.wilddynamos.bookapp.activity.ChangePasswordActivity;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.ws.local.GetMyProfile;
import com.wilddynamos.bookapp.ws.remote.action.Logout;

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
		startActivity(intent);
	}

	public void logOut(View view) {
		new Logout(this).execute();
	}

	public void changePassword(View view) {
		Intent intent = new Intent(this, ChangePasswordActivity.class);
		startActivity(intent);
	}

	// public Bitmap getBitmap(Context ctx, String
	// pathNameRelativeToAssetsFolder) {
	// InputStream bitmapIs = null;
	// Bitmap bmp = null;
	// try {
	// bitmapIs = ctx.getAssets().open(pathNameRelativeToAssetsFolder);
	// bmp = BitmapFactory.decodeStream(bitmapIs);
	// bitmapIs.close();
	// } catch (IOException e) {
	// }
	// return bmp;
	// }

}
