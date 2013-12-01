package com.wilddynamos.bookfinder.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.MultiWindowActivity;
import com.wilddynamos.bookfinder.activity.mybooks.MyRequestDetailActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class WithdrawRequest extends AsyncTask<String, Void, Boolean> {

	private MyRequestDetailActivity a;

	public WithdrawRequest(MyRequestDetailActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("bookId", params[0]);
		paramsMap.put("requesterId", params[1]);

		try {
			return "1".equals(DataUtils.receiveFlag(Connection.requestByPost(
					"/WithdrawRequest", paramsMap)));
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected void onPostExecute(Boolean success) { // after get result from
													// server
		if (success) {
			Toast.makeText(a, "Request withdrew!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class); // go to
																		// mybooks
																		// if
																		// get
																		// withdrew
			intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
			a.startActivity(intent);
		} else
			Toast.makeText(a, "Oops", Toast.LENGTH_SHORT).show();
	}
}
