package com.wilddynamos.bookfinder.activity.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.activity.MultiWindowActivity;
import com.wilddynamos.bookfinder.ws.remote.action.profile.ChangePassword;

public class ChangePasswordActivity extends Activity {

	private EditText oldPassword;
	private EditText newPassword;
	private EditText confirmation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_changepassword);

		oldPassword = (EditText) findViewById(R.id.oldPassword);
		newPassword = (EditText) findViewById(R.id.newPassword);
		confirmation = (EditText) findViewById(R.id.newpassword_confirm);

	}

	public void save(View view) {
		if ((oldPassword.getText().toString() == null || "".equals(oldPassword
				.getText().toString()))
				|| (newPassword.getText().toString() == null || ""
						.equals(newPassword.getText().toString()))
				|| (confirmation.getText().toString() == null || ""
						.equals(confirmation.getText().toString()))) {
			Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (!newPassword.getText().toString()
				.equals(confirmation.getText().toString())) {
			Toast.makeText(this, "Confirmation does not match",
					Toast.LENGTH_SHORT).show();
			return;
		}

		new ChangePassword(this).execute(new String[] {
				oldPassword.getText().toString(),
				newPassword.getText().toString() });
	}

	public void cancel(View view) {
		Intent intent = new Intent(this, MultiWindowActivity.class);
		intent.putExtra(MultiWindowActivity.TAB_SELECT, 2);
		startActivity(intent);
	}
}
