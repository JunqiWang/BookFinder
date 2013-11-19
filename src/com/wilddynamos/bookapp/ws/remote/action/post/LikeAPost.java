package com.wilddynamos.bookapp.ws.remote.action.post;

import java.util.HashMap;
import java.util.Map;

import com.wilddynamos.bookapp.activity.post.PostDetailsActivity;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.DataUtils;

public class LikeAPost extends Thread {
	
	private PostDetailsActivity a;
	
	private int id;
	
	public LikeAPost(PostDetailsActivity a, int id) {
		this.a = a;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id + "");
			
			if(DataUtils.receiveFlag(Connection.requestByGet("/Like", map)).equals("1"))
				a.getHandler().sendEmptyMessage(2);
			else
				a.getHandler().sendEmptyMessage(-2);
			
		} catch(Exception e) {
			a.getHandler().sendEmptyMessage(-2);
		}
	}
}
