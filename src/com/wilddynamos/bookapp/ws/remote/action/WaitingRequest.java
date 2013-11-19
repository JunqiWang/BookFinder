package com.wilddynamos.bookapp.ws.remote.action;

import android.widget.Toast;

import com.wilddynamos.bookapp.activity.post.PostListActivity;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.DataUtils;

public class WaitingRequest extends Thread {
	
	public static String s = "K";
	
	private PostListActivity a;
	
	public WaitingRequest(PostListActivity a) {
		this.a = a;
	}
	
	@Override
	public void run() {
		s = DataUtils.receiveFlag(Connection.waiting());
		
		a.getHandler().sendEmptyMessage(9);
	}
}
