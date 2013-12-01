package com.wilddynamos.bookfinder.ws.remote.action.mybooks;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookfinder.activity.MultiWindowActivity;
import com.wilddynamos.bookfinder.activity.mybooks.MyPostDetailActivity;
import com.wilddynamos.bookfinder.utils.DataUtils;
import com.wilddynamos.bookfinder.ws.remote.Connection;

public class DeleteBook extends AsyncTask<String, Void, Boolean> {

	private MyPostDetailActivity a;

	public DeleteBook(MyPostDetailActivity a) {
		this.a = a;
	}

	@Override
	protected Boolean doInBackground(String... params) {

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);

		try {
			return "1".equals(DataUtils.receiveFlag(Connection.requestByGet(
					"/DeleteBook", paramsMap)));
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			Toast.makeText(a, "This post has been deleted.",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class);
			intent.putExtra(MultiWindowActivity.TAB_SELECT, 1);
			a.startActivity(intent);
		} else
			Toast.makeText(a, "Oops!", Toast.LENGTH_SHORT).show();
	}
}
