package com.wilddynamos.bookapp.ws.remote.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.SignupActivity;
import com.wilddynamos.bookapp.activity.mybooks.PostOrEditBookActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

//public class Signup extends Thread {
//	
//	private SignupActivity a;
//	
//	private String email, name, pwd;
//	
//	private Context context;
//	
//	public Signup(SignupActivity a, String email, String name, String pwd, Context context) {
//		this.a = a;
//		this.email = email;
//		this.name = name;
//		this.pwd = pwd;
//		this.context = context;
//	}
//
//	@Override
//	public void run() {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("email", email);
//		params.put("name", name);
//		params.put("password", pwd);
//		try{
//			InputStream is = Connection.requestByPost("/Signup", params);
//			BufferedReader br = new BufferedReader(new InputStreamReader(is));
//			String result = br.readLine();
//			
//			
//				if(result.equals("-1")) 
//					a.getHandler().sendEmptyMessage(-1);
//				else if (result.equals("-2"))
//					a.getHandler().sendEmptyMessage(-2);
//				else if (result.equals("1")) {
//					a.getHandler().sendEmptyMessage(1);
//					
//				}
//			}
//
//		catch(IOException e) {
//			e.printStackTrace();
//		}
//	}
//}

public class Signup extends AsyncTask<String, Void, String> {

	private SignupActivity a;
	private String email;
	private String name;
	private String password;
	
	public Signup(SignupActivity a) {
		this.a = a;
	}
	
	@Override
	protected String doInBackground(String... params) {
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
			Connection.login(email, password);
			UserDataSource userDataSource = new UserDataSource(context);
			userDataSource.open();
			User user = userDataSource.createUser(Connection.id, email.getEditableText().toString(), 
					password.getEditableText().toString(), 
					name.getEditableText().toString(), null, null, null, null, null);
			userDataSource.close();
			Toast.makeText(a, "Welcome, new friend!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class);
			intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
			a.startActivity(intent);
		} 
		else if (result.equals("-1"))
			Toast.makeText(a, "Failed", Toast.LENGTH_SHORT).show();
		else if (result.equals("-2"))
			Toast.makeText(a, "Email exists!", Toast.LENGTH_SHORT).show();
	}
}
