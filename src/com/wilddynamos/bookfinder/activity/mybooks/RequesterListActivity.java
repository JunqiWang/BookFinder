package com.wilddynamos.bookfinder.activity.mybooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.DeclineAll;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.GetRequestList;

public class RequesterListActivity extends Activity {

	private ListView requesterList;
	private Button declineButton;
	private LazyAdapter la;

	private int bookId; // book id for the requests

	private ArrayList<HashMap<String, String>> requesterArray;

	private List<Integer> ids;
	private int currentPage;

	private JSONArray jsonArray;
	private GetRequestList load;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_requestlist);

		bookId = getIntent().getIntExtra("id", 0); // get book id from the
													// former activity

		requesterList = (ListView) findViewById(R.id.request_list);
		declineButton = (Button) findViewById(R.id.declineButton);

		requesterArray = new ArrayList<HashMap<String, String>>();
		ids = new ArrayList<Integer>();

		refresh(); // refresh the page on create to get updated data

		declineButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // dialog for confirmation to decline
											// all requests
				new AlertDialog.Builder(RequesterListActivity.this)
						.setTitle("Decline all requests?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										DeclineAll da = new DeclineAll(
												RequesterListActivity.this);
										da.execute(new String[] { String
												.valueOf(bookId) });
									}
								})
						.setNegativeButton("No", null)
						.show();
			}
		});
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getBookId() {
		return bookId;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public LazyAdapter getLazyAdapter() {
		return la;
	}

	public void setJSONArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	private void refresh() {
		requesterList.setTop(50);

		currentPage = 1;

		try {
			load.cancel(true);
		} catch (Exception e) {
		}

		// new getrequestlist async task to get requesters data from server
		GetRequestList load = new GetRequestList(RequesterListActivity.this);
		load.execute(new String[] { String.valueOf(bookId),
				String.valueOf(currentPage) });
	}

	public void pour() { // clear the requester array and load data, using
							// adapter to set the list
		requesterArray.clear();
		loadData();

		la = new LazyAdapter(this, requesterArray, ids, bookId);

		requesterList.setAdapter(la);

		requesterList.setTop(0);
	}

	public void loadData() { // convert jsonArray data to hashmap, for following
								// setting
		if (jsonArray == null)
			return;

		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);

				ids.add(jo.getInt("id"));

				HashMap<String, String> map = new HashMap<String, String>();

				map.put("name", jo.getString("name"));
				try {
					map.put("photo", jo.getString("photo"));
				} catch (JSONException e) {
				}

				requesterArray.add(map);
			}

		} catch (JSONException e) {
		}

		if (jsonArray.length() > 0)
			currentPage++;
	}

}