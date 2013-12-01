package com.wilddynamos.bookfinder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.ws.remote.action.Signup;

public class SignupActivity extends Activity {

	private EditText email;
	private EditText name;
	private EditText password;
	private EditText confirmation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		email = (EditText) findViewById(R.id.userEmail);
		name = (EditText) findViewById(R.id.userName);
		password = (EditText) findViewById(R.id.userPassword);
		confirmation = (EditText) findViewById(R.id.userPasswordConfirm);
	}

	public void signUp(View view) {
		if ((password.getText().toString() == null || "".equals(password
				.getText().toString()))
				|| (name.getText().toString() == null || "".equals(name
						.getText().toString()))) {
			Toast.makeText(this, "Please fill in all fields",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!isValidEmail(email.getText().toString())) {
			Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!password.getEditableText().toString()
				.equals(confirmation.getEditableText().toString())) {
			Toast.makeText(this, "Confirmation not match", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		new Signup(this).execute(new String[] {
				email.getEditableText().toString(),
				name.getEditableText().toString(),
				password.getEditableText().toString() });
	}

	public boolean isValidEmail(CharSequence target) {
		if (target == null)
			return false;
		else
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
	}
}
