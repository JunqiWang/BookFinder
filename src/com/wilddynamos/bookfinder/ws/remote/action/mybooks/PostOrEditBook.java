package com.wilddynamos.bookfinder.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import com.wilddynamos.bookfinder.activity.MultiWindowActivity;
import com.wilddynamos.bookfinder.activity.mybooks.PostOrEditBookActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class PostOrEditBook extends AsyncTask<String, Void, Boolean> {

	private PostOrEditBookActivity a;
	
	public PostOrEditBook(PostOrEditBookActivity a) {
		this.a = a;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userId", Connection.id + "");
		paramsMap.put("isRent", params[0]);
		paramsMap.put("id", params[1]);
		paramsMap.put("name", params[2]);
		paramsMap.put("cover", params[3]);
		paramsMap.put("price", params[4]);
		paramsMap.put("description", params[5]);
		
		if("true".equals(params[0])) {
			paramsMap.put("perValue", params[6]);
			paramsMap.put("duration", params[7]);
			
		}
		
		return "1".equals(DataUtils
				.receiveFlag(Connection.requestByPost("/PostEditBook", paramsMap)));
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if(success) {
			Toast.makeText(a, "Success", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class);
			intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
			a.startActivity(intent);
		} else
			Toast.makeText(a, "Failed", Toast.LENGTH_SHORT).show();
	}
}
