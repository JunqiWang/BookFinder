package com.wilddynamos.bookfinder.activity.mybooks;

import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.activity.BaseBookDetailActivity;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.DeleteBook;

public class MyPostDetailActivity extends BaseBookDetailActivity {

	private TextView requesterNum;
	private Button seeRequest;

	private boolean sOrR;
	private boolean per;
	private int duration;
	private String description;
	private String coverString;

	@Override
	protected void createFunctionSpecificView() {

		((LinearLayout) findViewById(R.id.bookDetail_myPost_requestInfo))
				.setVisibility(LinearLayout.VISIBLE);
		((LinearLayout) findViewById(R.id.bookDetail_myPost_buttons))
				.setVisibility(LinearLayout.VISIBLE);

		requesterNum = (TextView) findViewById(R.id.bookDetail_myPost_requesterNum);
		seeRequest = (Button) findViewById(R.id.bookDetail_myPost_seeRequesters);
	}

	@Override
	protected String[] getParams() {
		return new String[] { id + "", "isMine" };
	}

	@Override
	protected void fillFunctionSpecificView(final JSONObject jo) {
		try {
			try {
				coverString = jo.getString("cover");
			} catch (JSONException e) {
			}

			if (jo.getBoolean("sOrR")) {
				this.sOrR = true;

				if (!jo.getBoolean("hasMadeRespond"))
					title.setText("Selling this book");
				else
					title.setText("Have sold this book");

			} else {
				this.sOrR = false;
				System.out.println("reached0");
				this.per = jo.getBoolean("per");
				System.out.println("reached1");
				this.duration = jo.getInt("availableTime");
				System.out.println("reached2");

				if (!jo.getBoolean("hasMadeRespond"))
					title.setText("Renting this book");
				else
					title.setText("Have rented this book out");
			}

			this.description = jo.getString("description");

			if (!jo.getBoolean("hasMadeRespond")) {

				requesterNum.setText("   " + jo.getInt("requesterNum")
						+ " request"
						+ (jo.getInt("requesterNum") <= 1 ? "" : "s"));

				if (jo.getInt("requesterNum") == 0)
					seeRequest.setClickable(false);
				else
					seeRequest.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									MyPostDetailActivity.this,
									RequesterListActivity.class);

							intent.putExtra("id", id);

							MyPostDetailActivity.this.startActivity(intent);
						}
					});

			} else {
				requesterNum.setText("Who's using?");
				seeRequest.setText("See");

				OnClickListener ocl = new OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(MyPostDetailActivity.this)
								.setTitle(
										"You cannot modify because it is on using.")
								.setPositiveButton("OK", null).show();
					}
				};
				((Button) findViewById(R.id.bookDetail_myPost_edit))
						.setOnClickListener(ocl);
				((Button) findViewById(R.id.bookDetail_myPost_delete))
						.setOnClickListener(ocl);

				seeRequest.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MyPostDetailActivity.this,
								RequesterProfileActivity.class);

						try {
							intent.putExtra("id", jo.getInt("requesterId"));
							intent.putExtra("bookId", id);
							intent.putExtra("responded", true);
						} catch (JSONException e) {
						}

						MyPostDetailActivity.this.startActivity(intent);
					}
				});
			}
			like.setClickable(false);

		} catch (JSONException e) {
		}

	}

	public void editPost(View view) {

		Intent intent = new Intent(this, PostOrEditBookActivity.class);

		intent.putExtra("id", id);
		intent.putExtra("isPost", false);
		intent.putExtra("name", name.getText().toString());
		intent.putExtra(
				"price",
				price.getText().toString()
						.substring(0, price.getText().toString().indexOf(' ')));
		intent.putExtra("sOrR", sOrR);
		if (!sOrR) {
			intent.putExtra("per", per);
			intent.putExtra("duration", duration);
		}
		intent.putExtra("description", description);

		if (coverString != null) {
			byte[] bytes = coverString.getBytes(Charset.forName("ISO-8859-1"));
			intent.putExtra("BMP", bytes);
		}

		startActivity(intent);
	}

	public void deletePost(View view) {
		new AlertDialog.Builder(this)
				.setTitle("Delete this post?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DeleteBook(MyPostDetailActivity.this).execute(new String[] { id + "" });
					}
				})
				.setNegativeButton("No", null)
				.show();
	}
}
