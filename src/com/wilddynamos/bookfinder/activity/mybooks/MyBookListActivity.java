package com.wilddynamos.bookfinder.activity.mybooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.ws.remote.Connection;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.GetMyBooks;

public class MyBookListActivity extends Activity {

	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;

	private List<Integer> sellIds;
	private List<Integer> rentIds;
	private List<Integer> buyIds;
	private List<Integer> borrowIds;

	private int position = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_list);

		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		sellIds = new ArrayList<Integer>();
		rentIds = new ArrayList<Integer>();
		buyIds = new ArrayList<Integer>();
		borrowIds = new ArrayList<Integer>();

		listDataHeader = new ArrayList<String>();
		listDataHeader.add("Sell");
		listDataHeader.add("Rent");
		listDataHeader.add("Buy");
		listDataHeader.add("Borrow");
		listDataChild = new HashMap<String, List<String>>();

		// new a getmybooks async task to get books of user id
		GetMyBooks gmb = new GetMyBooks(this);
		gmb.execute(new String[] { String.valueOf(Connection.id) });

	}

	@Override
	protected void onRestart() {
		super.onRestart();

		GetMyBooks gmb = new GetMyBooks(this);
		gmb.execute(new String[] { String.valueOf(Connection.id) });
	}

	public void loadData(JSONArray jsonArray) { // get data from the server and
												// set headers and children for
												// different kind of list
		// new 4 headers
		List<String> sell = new ArrayList<String>();
		List<String> rent = new ArrayList<String>();
		List<String> buy = new ArrayList<String>();
		List<String> borrow = new ArrayList<String>();

		if (jsonArray == null)
			return;

		// set books to different lists according to the type and sOrR
		// attributes
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);

				// ids.add(jo.getInt("id"));
				int id = jo.getInt("id");

				int type = jo.getInt("type");
				Boolean sOrR = jo.getBoolean("sOrR");
				String name = jo.getString("name");

				if (type == 1) {
					if (sOrR) {
						sell.add(name);
						sellIds.add(id);
					} else {
						rent.add(name);
						rentIds.add(id);
					}
				} else if (type == 0) {
					if (sOrR) {
						buy.add(name);
						buyIds.add(id);
					} else {
						borrow.add(name);
						borrowIds.add(id);
					}
				}
			}

			listDataChild.put(listDataHeader.get(0), sell);
			listDataChild.put(listDataHeader.get(1), rent);
			listDataChild.put(listDataHeader.get(2), buy);
			listDataChild.put(listDataHeader.get(3), borrow);

		} catch (JSONException e) {
		}

	}

	public void showBookForSaleInfo(View view, int childPosition) {
		Intent intent = new Intent(this, MyPostDetailActivity.class);
		intent.putExtra("id", sellIds.get(childPosition));
		startActivity(intent);
	}

	public void showBookForRentInfo(View view, int childPosition) {

		Intent intent = new Intent(this, MyPostDetailActivity.class);
		intent.putExtra("id", rentIds.get(childPosition));
		startActivity(intent);
	}

	public void showBuyRequestDetails(View view, int childPosition) {
		Intent intent = new Intent(this, MyRequestDetailActivity.class);
		intent.putExtra("id", buyIds.get(childPosition));
		startActivity(intent);
	}

	public void showBorrowRequestDetails(View view, int childPosition) {
		Intent intent = new Intent(this, MyRequestDetailActivity.class);
		intent.putExtra("id", borrowIds.get(childPosition));
		startActivity(intent);
	}

	public void clearList() {
		listDataChild.clear();

		sellIds.clear();
		rentIds.clear();
		buyIds.clear();
		borrowIds.clear();
	}

	public void fill() {
		// set the adapter
		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);
		expListView.setAdapter(listAdapter);

		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// set other lists to collapse when one is open
				position = arg0;
				for (int i = 0; i < listAdapter.getGroupCount(); i++) {
					if (arg0 != i) {
						expListView.collapseGroup(i);
					}
				}

			}

		});
		expListView.setGroupIndicator(null);
		// set listener for each list's children according to their types
		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView expandablelistview,
					View clickedView, int groupPosition, int childPosition,
					long childId) {
				switch (groupPosition) {
				case 0:
					showBookForSaleInfo(clickedView, childPosition);
					break;
				case 1:
					showBookForRentInfo(clickedView, childPosition);
					break;
				case 2:
					showBuyRequestDetails(clickedView, childPosition);
					break;
				case 3:
					showBorrowRequestDetails(clickedView, childPosition);
					break;
				default:
					break;
				}

				return false;
			}
		});

		if (position != -1)
			expListView.expandGroup(position);
	}
}
