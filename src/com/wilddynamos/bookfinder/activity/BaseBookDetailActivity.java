package com.wilddynamos.bookfinder.activity;

import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.utils.ZoomInOutAction;
import com.wilddynamos.bookfinder.ws.remote.action.GetBookDetail;
import com.wilddynamos.bookfinder.ws.remote.action.post.LikeAPost;

/**
 * Shared part of book detail page, shared by post detail, my posted book
 * detail, and my requested book detail
 * 
 * @author JunqiWang
 * 
 */
public abstract class BaseBookDetailActivity extends Activity {

	/**
	 * image to block the view while loading data
	 */
	protected ImageView bg;

	// View widgets
	protected TextView title;
	protected TextView name;
	protected TextView price;
	protected TextView availableTime;
	protected ImageView cover;
	protected TextView description;
	protected ImageView like;
	protected TextView likeNum;

	/**
	 * id of this book
	 */
	protected int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_bookdetail);

		bg = (ImageView) findViewById(R.id.bookDetail_block);

		title = (TextView) findViewById(R.id.bookDetail_title);
		name = (TextView) findViewById(R.id.bookDetail_name);
		price = (TextView) findViewById(R.id.bookDetail_price);
		availableTime = (TextView) findViewById(R.id.bookDetail_availableTime);
		cover = (ImageView) findViewById(R.id.bookDetail_cover);
		description = (TextView) findViewById(R.id.bookDetail_description);
		like = (ImageView) findViewById(R.id.bookDetail_like);
		likeNum = (TextView) findViewById(R.id.bookDetail_likeNum);

		id = getIntent().getExtras().getInt("id");

		// call this to generate different layout for the three kinds of detail
		// pages
		createFunctionSpecificView();

		new GetBookDetail(this).execute(getParams());

		description.setMovementMethod(new ScrollingMovementMethod());
	}

	/**
	 * Should be implemented to generate specific layout
	 */
	protected abstract void createFunctionSpecificView();

	/**
	 * Should be implemented to pass specific parameters
	 * 
	 * @return the parameter used by network connection
	 */
	protected abstract String[] getParams();

	/**
	 * Fill the view widgets while data is available
	 * 
	 * @param jsonArray
	 */
	public final void fill(JSONArray jsonArray) {
		if (jsonArray == null || jsonArray.length() == 0)
			return;

		JSONObject jo = null;
		String s = null;
		try {
			jo = jsonArray.getJSONObject(0);

			name.setText(jo.getString("name"));
			description.setText(jo.getString("description"));
			likeNum.setText(jo.getString("likes"));

			if (jo.getBoolean("sOrR"))
				price.setText(jo.getString("price") + " ");
			else {
				price.setText(jo.getString("price") + " / "
						+ (jo.getBoolean("per") ? "month" : "week"));
				availableTime.setText("Available for "
						+ jo.getInt("availableTime")
						+ (jo.getBoolean("per") ? " months" : " weeks"));
			}

			s = jo.getString("cover");

		} catch (JSONException e) {
		}

		if (s != null && !"".equals(s)) {
			byte[] cover = s.getBytes(Charset.forName("ISO-8859-1"));
			Bitmap coverImage = BitmapFactory.decodeByteArray(cover, 0,
					cover.length);
			this.cover.setImageBitmap(coverImage);
		}

		like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new LikeAPost(BaseBookDetailActivity.this)
						.execute(new String[] { id + "" });
			}
		});

		fillFunctionSpecificView(jo);
		bg.setAlpha(0f);
	}

	/**
	 * Fill distinct views owned by the three pages
	 * 
	 * @param jo
	 */
	protected abstract void fillFunctionSpecificView(JSONObject jo);

	/**
	 * Like this book
	 */
	public final void setLikeNum() {
		String s = (Integer.parseInt(likeNum.getText().toString()) + 1) + "";
		likeNum.setText(s);
	}
	
	/**
	 * Show the book cover in full screen
	 * 
	 * @param view
	 * @author Cheng Zhang
	 */
	public final void zoomInOut(View view) {
		ZoomInOutAction.action(this, cover);
	}
}
