package com.wilddynamos.bookapp.ws.remote.action.post;

import java.util.HashMap;
import java.util.Map;

import com.wilddynamos.bookapp.activity.post.PostListActivity;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.DataUtils;
import com.wilddynamos.bookapp.ws.remote.action.WaitingRequest;

public class GetPostList extends Thread {
	
	private PostListActivity a;
	
	public GetPostList(PostListActivity a) {
		this.a = a;
//		new WaitingRequest(a).start();
	}

	@Override
	public void run() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("currentPage", a.getCurrentPage() + "");
			map.put("sOrR", a.getsOrR());
			map.put("search", a.getSearch());
			
			a.setJSONArray(DataUtils.receiveJSON(Connection.requestByGet("/GetPosts", map)));

			a.getHandler().sendEmptyMessage(1);
			
		} catch(Exception e) {
			a.getHandler().sendEmptyMessage(-1);
		}
	}
}
