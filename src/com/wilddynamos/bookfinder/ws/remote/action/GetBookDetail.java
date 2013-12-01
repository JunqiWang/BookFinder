package com.wilddynamos.bookfinder.ws.remote.action;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.wilddynamos.bookfinder.activity.BaseBookDetailActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

import android.os.AsyncTask;
import android.widget.Toast;

/** Get book detail action **/
public class GetBookDetail extends AsyncTask<String, Void, JSONArray> {

	private BaseBookDetailActivity bda;

	public GetBookDetail(BaseBookDetailActivity bda) {
		this.bda = bda;
	}

	/** get book detail from server **/
	@Override
	protected JSONArray doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		if (params.length >= 2)
			paramsMap.put(params[1], "1");

		try {
			return DataUtils.receiveJSON(Connection.requestByPost(
					"/GetBookDetail", paramsMap));

		} catch (Exception e) {
			return null;
		}
	}

	/** post result **/
	@Override
	protected void onPostExecute(JSONArray jsonArray) {
		if (bda != null)
			if (jsonArray == null || jsonArray.length() == 0)
				Toast.makeText(bda, "Oops", Toast.LENGTH_SHORT).show();
			else
				bda.fill(jsonArray);
	}
}
