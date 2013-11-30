package com.wilddynamos.bookapp.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.mybooks.RequesterListActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class GetRequestList extends AsyncTask<String, Void, String> {

	private RequesterListActivity a;

	
	public GetRequestList(RequesterListActivity a) {
		this.a = a;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try{
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("id", params[0]);
			paramsMap.put("currentPage", params[1]);
			System.out.println(paramsMap);
		
			a.setJSONArray(DataUtils.receiveJSON(Connection.requestByGet("/GetRequests", paramsMap)));
		
			return "1";
		}
		catch(Exception e) {
			return "-1";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("1")) {
			if(a.getCurrentPage() == 1)
				a.pour();
			else {
				a.loadData();
				a.getLazyAdapter().notifyDataSetChanged();
//				a.getLoadProgress().setVisibility(ProgressBar.INVISIBLE);
			}
		} 
		else if (result.equals("-1"))
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(a, "What happened?", Toast.LENGTH_SHORT).show();
	}
}

