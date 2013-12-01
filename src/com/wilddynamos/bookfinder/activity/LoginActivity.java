package com.wilddynamos.bookfinder.activity;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.dblayout.RememberMeDbHelper;
import com.wilddynamos.bookfinder.dblayout.RememberMeContract.RememberMeColumn;
import com.wilddynamos.bookfinder.ws.remote.action.ForgotPwd;
import com.wilddynamos.bookfinder.ws.remote.action.Login;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/** Activity for Login in **/
public class LoginActivity extends Activity {

	private EditText email;
	private EditText password;
	private Button login;
	public ImageView block;

	private ForgotPwd forgotPwd;

	private CheckBox rememberme;
	private RememberMeDbHelper mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		email = (EditText) findViewById(R.id.userInputEmail);
		password = (EditText) findViewById(R.id.userInputPassword);
		login = (Button) findViewById(R.id.login);
		rememberme = (CheckBox) findViewById(R.id.checkbox_remember);
		block = (ImageView) findViewById(R.id.login_block);

		mDbHelper = new RememberMeDbHelper(getApplicationContext());
		// If the user is remembered, it will automatically log in
		setRemember();
		// Validation for log in
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!"".equals(email.getText().toString())
						&& !"".equals(password.getText().toString())) {
					
					Login login = new Login(LoginActivity.this);
					login.execute(new String[] { email.getText().toString(),
							password.getText().toString() });

					if (rememberme.isChecked())
						remember();
					else
						notRemember();
				}
				// signIn();
			}
		});

	}

	/**
	 * If user return from other activity and the user is remembered, it will
	 * automatically log in
	 **/
	@Override
	protected void onRestart() {
		super.onRestart();
		setRemember();
		block.setAlpha(0f);
	}

	/** redirect to post list page **/
	public void signIn() {
		Intent intent = new Intent(this, MultiWindowActivity.class);
		startActivity(intent);
	}

	/** redirect to sign up page **/
	public void signUp(View view) {
		Intent intent = new Intent(this, SignupActivity.class);
		startActivity(intent);
	}

	/** forget password **/
	public void forgotPassword(View view) {
		final EditText email = new EditText(this);
		forgotPwd = new ForgotPwd(this);
		new AlertDialog.Builder(this)
				.setTitle("Enter your email")
				.setView(email)
				.setPositiveButton("Send",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if ("".equals(email.getText().toString()))
									new AlertDialog.Builder(LoginActivity.this)
											.setTitle("You entered nothing.")
											.setPositiveButton("OK", null)
											.show();
								else
									forgotPwd.execute(new String[] { email
											.getText().toString() });
							}
						}).setNegativeButton("Cancel", null).show();
	}

	/** choose if remember me **/
	public void onCheckboxClicked(View view) {
		if (((CheckBox) view).isChecked()) {
			Toast.makeText(LoginActivity.this,
					"Warning:Your password will be saved", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/** save log in data to database when user select remember me **/
	public void remember() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		String myemail = email.getText().toString();
		String mypassword = password.getText().toString();

		ContentValues values = new ContentValues();
		values.put(RememberMeColumn.COLUMN_NAME_C1, myemail);
		values.put(RememberMeColumn.COLUMN_NAME_C2, mypassword);

		db.insert(RememberMeColumn.TABLE_NAME, null, values);
		db.close();
	}

	/** when user do not select remember me, clear the database **/
	public void notRemember() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(RememberMeColumn.TABLE_NAME, null, null);
		db.close();
	}

	/** automatically log in when the user is remembered **/
	public void setRemember() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ RememberMeColumn.TABLE_NAME + "'", null);
		if (cursor.getCount() <= 0 || cursor == null) {
			rememberme.setChecked(false);
			cursor.close();
			db.close();
			block.setAlpha(0f);
			return;
		}
		String[] projection = { RememberMeColumn.COLUMN_NAME_C1,
				RememberMeColumn.COLUMN_NAME_C2 };

		String sortOrder = RememberMeColumn._ID + " DESC";

		cursor = db.query(RememberMeColumn.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		if (cursor.getCount() <= 0) {
			rememberme.setChecked(false);
			cursor.close();
			block.setAlpha(0f);
			return;
		}
		cursor.moveToFirst();

		String myemail = cursor.getString(cursor
				.getColumnIndex(RememberMeColumn.COLUMN_NAME_C1));
		String mypassword = cursor.getString(cursor
				.getColumnIndex(RememberMeColumn.COLUMN_NAME_C2));
		rememberme.setChecked(true);
		email.setText(myemail);
		password.setText(mypassword);
		cursor.close();
		db.close();

		String is_logout = getIntent().getStringExtra("logout");
		if (is_logout == null) {
			Login login = new Login(LoginActivity.this);
			login.execute(new String[] { myemail, mypassword });
		} else
			block.setAlpha(0f);
	}
}