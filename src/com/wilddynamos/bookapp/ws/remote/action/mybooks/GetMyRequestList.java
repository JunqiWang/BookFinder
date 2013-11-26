package com.wilddynamos.bookapp.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import com.wilddynamos.bookapp.activity.mybooks.RequesterListActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class GetMyRequestList extends Thread {
	
	private RequesterListActivity a;
	
	public GetMyRequestList(RequesterListActivity a) {
		this.a = a;
	}
	
	@Override
	public void run() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("currentPage", a.getCurrentPage() + "");
//			map.put("sOrR", a.getsOrR());
//			map.put("search", a.getSearch());
//			
//			a.setJSONArray(DataUtils.receiveJSON(Connection.requestByGet("/GetPosts", map)));

			a.getHandler().sendEmptyMessage(1);
			
		} catch(Exception e) {
			a.getHandler().sendEmptyMessage(-1);
		}
	}
}
