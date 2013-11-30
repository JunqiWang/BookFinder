package com.wilddynamos.bookapp.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.SignupActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class Signup extends AsyncTask<String, Void, String> {

	private SignupActivity a;
	private Context context;
	
	private String email;
	private String name;
	private String password;
	
	public Signup(SignupActivity a, Context context) {
		this.a = a;
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {		//background: send email, name and pwd to server to signup as a new user
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		email = params[0];
		name = params[1];
		password = params[2];
		
		paramsMap.put("email", email);
		paramsMap.put("name", name);
		paramsMap.put("password", password);
		
		return DataUtils.receiveFlag(Connection.requestByPost("/Signup", paramsMap));
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("1")) {
			
			new Thread() {				//new thread to add new user into sqlite
				@Override
				public void run() {
					Connection.login(email, password);
					UserDataSource userDataSource = new UserDataSource(context);
					userDataSource.open();
					userDataSource.createUser(Connection.id, email, 
							password, name, null, null, null, null, null);
					userDataSource.close();
				}
			}.start();
			
			Toast.makeText(a, "Welcome, new friend!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class);	//same as pages after login
			//intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
			a.startActivity(intent);
		} 
		else if (result.equals("-1"))
			Toast.makeText(a, "Failed", Toast.LENGTH_SHORT).show();
		else if (result.equals("-2"))
			Toast.makeText(a, "Email exists!", Toast.LENGTH_SHORT).show();
	}
}
