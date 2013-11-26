package com.wilddynamos.bookapp.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.wilddynamos.bookapp.activity.mybooks.PostOrEditBookActivity;
import com.wilddynamos.bookapp.activity.post.PostDetailsActivity;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

import android.os.AsyncTask;
import android.widget.Toast;

public class GetBookDetail extends AsyncTask<String, Void, JSONArray> {

	private PostDetailsActivity pda = null;
	private PostOrEditBookActivity peba = null;
	
	public GetBookDetail(PostDetailsActivity pda) {
		this.pda = pda;
	}
	
	public GetBookDetail(PostOrEditBookActivity peba) {
		this.peba = peba;
	}
	
	
	@Override
	protected JSONArray doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		if(params.length >= 2)
			paramsMap.put("isEdit", "1");
		
		try {
			return DataUtils.receiveJSON(
					Connection.requestByPost("/GetBookDetail", paramsMap));
			
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONArray jsonArray) {
		if(pda != null)
			if(jsonArray == null || jsonArray.length() == 0)
				Toast.makeText(pda, "Oops", Toast.LENGTH_SHORT).show();
			else
				pda.fill(jsonArray);
		
		if(peba != null)
			if(jsonArray == null || jsonArray.length() == 0)
				Toast.makeText(peba, "Oops", Toast.LENGTH_SHORT).show();
			else
				peba.fill(jsonArray);
	}
}
