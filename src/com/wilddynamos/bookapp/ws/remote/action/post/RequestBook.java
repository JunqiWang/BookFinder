package com.wilddynamos.bookapp.ws.remote.action.post;

import java.util.HashMap;
import java.util.Map;

import com.wilddynamos.bookapp.activity.post.PostDetailsActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class RequestBook extends Thread {
	
	private PostDetailsActivity a;
	
	private int id;
	
	private String message;
	
	public RequestBook(PostDetailsActivity a, int id, String message) {
		this.a = a;
		this.id = id;
		this.message = message;
	}

	@Override
	public void run() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id + "");
			map.put("message", message);
			
			if(DataUtils.receiveFlag(Connection.requestByPost("/RequestBook", map)).equals("1"))
				a.getHandler().sendEmptyMessage(3);
			else
				a.getHandler().sendEmptyMessage(-3);
			
		} catch(Exception e) {
			a.getHandler().sendEmptyMessage(-3);
		}
	}
}
