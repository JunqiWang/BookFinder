package com.wilddynamos.bookfinder.activity.mybooks;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.AcceptRequest;

public class LazyAdapter extends BaseAdapter {

	private RequesterListActivity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	private List<Integer> ids;
	private int bookId;

	public LazyAdapter(RequesterListActivity a,
			ArrayList<HashMap<String, String>> d, List<Integer> ids, int bookId) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ids = ids;
		this.bookId = bookId;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) // inflate the view
			vi = inflater.inflate(R.layout.mybooks_requestlist_item, null);

		TextView requesterName = (TextView) vi.findViewById(R.id.requesterName); // title
		ImageView profileImage = (ImageView) vi
				.findViewById(R.id.mybooks_requester_image);
		Button viewButton = (Button) vi.findViewById(R.id.viewButton);
		Button acceptButton = (Button) vi.findViewById(R.id.acceptButton);

		final int pos = position;
		acceptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(activity)
						// a dialog box for user to confirm the accept operation
						.setTitle("Accept this request?")
						.setMessage(
								"Caution: This will automatically decline all other requesters.")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									// after user click the yes button, new a
									// relative asynctask to connect to the
									// server
									public void onClick(DialogInterface dialog,
											int which) {
										AcceptRequest ar = new AcceptRequest(
												activity);
										String[] params = {
												String.valueOf(activity
														.getBookId()),
												String.valueOf(activity
														.getIds().get(pos)) };
										ar.execute(params);
									}
								}).setNegativeButton("No", null)
						.show();
			}
		});

		viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showRequester(pos);
			}
		});

		HashMap<String, String> requester = new HashMap<String, String>();
		requester = data.get(position); // get data according to the position

		requesterName.setText(requester.get("name"));
		String s = requester.get("photo"); // decode the byte array to string
											// than to bitmap
		if (s != null && !"".equals(s)) {
			byte[] image = s.getBytes(Charset.forName("ISO-8859-1"));
			Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
					image.length);
			profileImage.setImageBitmap(bitmap);
		}

		return vi;
	}

	public void showRequester(int position) {
		Intent intent = new Intent(activity, RequesterProfileActivity.class);
		intent.putExtra("id", ids.get(position));
		intent.putExtra("bookId", bookId);
		activity.startActivity(intent);
	}
}