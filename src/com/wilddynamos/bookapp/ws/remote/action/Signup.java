package com.wilddynamos.bookapp.ws.remote.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.wilddynamos.bookapp.activity.SignupActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class Signup extends Thread {
	
	private SignupActivity a;
	
	private String email, name, pwd;
	
	private Context context;
	
	public Signup(SignupActivity a, String email, String name, String pwd, Context context) {
		this.a = a;
		this.email = email;
		this.name = name;
		this.pwd = pwd;
		this.context = context;
	}

	@Override
	public void run() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("name", name);
		params.put("password", pwd);
		try{
			InputStream is = Connection.requestByPost("/Signup", params);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String result = br.readLine();
			
			
				if(result.equals("-1")) 
					a.getHandler().sendEmptyMessage(-1);
				else if (result.equals("-2"))
					a.getHandler().sendEmptyMessage(-2);
				else if (result.equals("1")) {
					a.getHandler().sendEmptyMessage(1);
					
				}
			}

		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
