package com.wilddynamos.bookapp.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.wilddynamos.bookapp.activity.BaseBookDetailActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

import android.os.AsyncTask;
import android.widget.Toast;

public class GetBookDetail extends AsyncTask<String, Void, JSONArray> {

	private BaseBookDetailActivity bda;
	
	public GetBookDetail(BaseBookDetailActivity bda) {
		this.bda = bda;
	}
	
	@Override
	protected JSONArray doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		if(params.length >= 2)
			paramsMap.put(params[1], "1");
		
		try {
			return DataUtils.receiveJSON(Connection
						.requestByPost("/GetBookDetail", paramsMap));
			
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {
		if(bda != null)
			if(jsonArray == null || jsonArray.length() == 0)
				Toast.makeText(bda, "Oops", Toast.LENGTH_SHORT).show();
			else
				bda.fill(jsonArray);
	}
}
