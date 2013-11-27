package com.wilddynamos.bookapp.activity;

import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.utils.ZoomInOutAction;
import com.wilddynamos.bookapp.ws.remote.action.GetBookDetail;
import com.wilddynamos.bookapp.ws.remote.action.post.LikeAPost;

public abstract class BaseBookDetailActivity extends Activity {
	
	protected ImageView bg;
	protected TextView title;
	protected TextView name;
	protected TextView price;
	protected TextView availableTime;
	protected ImageView cover;
	protected TextView description;
	protected ImageView like;
	protected TextView likeNum;
	
	protected int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_bookdetail);
		
		bg = (ImageView) findViewById(R.id.bookdetails_background11);
		
		title = (TextView) findViewById(R.id.bookDetail_title);
		name = (TextView) findViewById(R.id.bookDetail_name);
		price = (TextView) findViewById(R.id.bookDetail_price);
		availableTime = (TextView) findViewById(R.id.bookDetail_availableTime);
		cover = (ImageView) findViewById(R.id.bookDetail_cover);
		description = (TextView) findViewById(R.id.bookDetail_description);
		like = (ImageView) findViewById(R.id.bookDetail_like);
		likeNum  = (TextView) findViewById(R.id.bookDetail_likeNum);
		
		id = getIntent().getExtras().getInt("id");
		
		createFunctionSpecificView();
		
		new GetBookDetail(this).execute(getParams());
	}
	
	protected abstract void createFunctionSpecificView();
	
	protected abstract String[] getParams();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void fill(JSONArray jsonArray) {
		if(jsonArray == null || jsonArray.length() == 0)
			return;
		
		JSONObject jo = null;
		String s = null;
		try {
			jo = jsonArray.getJSONObject(0);
			
			name.setText(jo.getString("name"));
			description.setText(jo.getString("description"));
			likeNum.setText(jo.getString("likes"));
			
			if(jo.getBoolean("sOrR"))
				price.setText(jo.getString("price") + " ");
			else {
				price.setText(jo.getString("price") 
						+ " / " + (jo.getBoolean("per") ? "week" : "month"));
				availableTime.setText("Available for " 
						+ jo.getInt("availableTime") + (jo.getBoolean("per") ? " months" : " weeks"));
			}
			
			s = jo.getString("cover");
			
		} catch (JSONException e) {
		}
		
		if(s != null && !"".equals(s)) {
			byte[] cover = s.getBytes(Charset.forName("ISO-8859-1"));
			System.out.println(cover.length);
			Bitmap coverImage = BitmapFactory.decodeByteArray(cover, 0, cover.length);
			this.cover.setImageBitmap(coverImage);
		}
		
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LikeAPost(BaseBookDetailActivity.this).execute(new String[]{id + ""});
			}
		});
		
		fillFunctionSpecificView(jo);
		bg.setAlpha(0.25f);
	}
	
	protected abstract void fillFunctionSpecificView(JSONObject jo);
	
	public void setLikeNum() {
		String s = (Integer.parseInt(likeNum.getText().toString()) + 1) + "";
		likeNum.setText(s);
	}
	
	public void zoomInOut(View view){
		ZoomInOutAction.action(this,cover);
	}
}
