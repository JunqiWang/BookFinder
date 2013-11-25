package com.wilddynamos.bookapp.ws.remote.action.post;

import java.util.HashMap;
import java.util.Map;

import com.wilddynamos.bookapp.activity.post.PostDetailsActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class GetPostDetail extends Thread {
	
	private PostDetailsActivity a;
	
	private int id;
	
	public GetPostDetail(PostDetailsActivity a, int id) {
		this.a = a;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id + "");
			
			a.setJSONArray(DataUtils.receiveJSON(Connection.requestByGet("/GetBookDetail", map)));

			a.getHandler().sendEmptyMessage(1);
			
		} catch(Exception e) {
			a.getHandler().sendEmptyMessage(-1);
		}
	}
}
