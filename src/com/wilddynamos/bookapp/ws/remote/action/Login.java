package com.wilddynamos.bookapp.ws.remote.action;

import com.wilddynamos.bookapp.activity.LoginActivity;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class Login extends Thread {
	
	private LoginActivity a;
	
	private String email, pwd;
	
	public Login(LoginActivity a, String email, String pwd) {
		this.a = a;
		this.email = email;
		this.pwd = pwd;
	}

	@Override
	public void run() {
		if(Connection.login(email, pwd)) {
			a.getHandler().sendEmptyMessage(1);
		} else
			a.getHandler().sendEmptyMessage(-1);
	}
}
