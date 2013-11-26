package com.wilddynamos.bookapp.ws.remote.action;

import com.wilddynamos.bookapp.activity.LoginActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class Logout extends Thread {
	private LoginActivity a;
	
	public Logout(LoginActivity a) {
		this.a = a;
	}

	@Override
	public void run() {
//		if("1".equals(DataUtils.receiveFlag(Connection.requestByGet("/Logout", null))))
//			a.getHandler().sendEmptyMessage(1);
//		else
//			a.getHandler().sendEmptyMessage(-1);
	}
}
