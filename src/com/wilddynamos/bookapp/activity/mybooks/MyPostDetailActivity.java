package com.wilddynamos.bookapp.activity.mybooks;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.BaseBookDetailActivity;
import com.wilddynamos.bookapp.activity.ZoomInOutActivity;

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
		return new String[]{id + "", "isMine"};
	}

	@Override
	protected void fillFunctionSpecificView(final JSONObject jo) {
		
		try {
			coverString = jo.getString("cover");
			if(jo.getBoolean("sOrR")) {
				this.sOrR = true;
				
				if(!jo.getBoolean("hasMadeRespond"))
					title.setText("Selling this book");
				else
					title.setText("Have sold this book");
				
			} else {
				this.sOrR = false;
				
				if(!jo.getBoolean("hasMadeRespond"))
					title.setText("Renting this book");
				else
					title.setText("Have rented this book out");
			}
			
			this.per = jo.getBoolean("per");
			this.duration = jo.getInt("availableTime");
			this.description = jo.getString("description");
			
			if(!jo.getBoolean("hasMadeRespond")) {
				
				requesterNum.setText(jo.getInt("requesterNum") + " request"
						+ (jo.getInt("requesterNum") <= 1 ? "" : "s"));
				
				if(jo.getInt("requesterNum") == 0)
					seeRequest.setClickable(false);
				else
					seeRequest.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									MyPostDetailActivity.this, RequesterListActivity.class);
							
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
								.setTitle("You cannot modify because it is on using.")
								.setPositiveButton("OK", null)
								.show();
					}
				};
				((Button) findViewById(R.id.bookDetail_myPost_edit))
						.setOnClickListener(ocl);
				((Button) findViewById(R.id.bookDetail_myPost_delete))
						.setOnClickListener(ocl);
				
				seeRequest.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								MyPostDetailActivity.this, RequesterDetailActivity.class);
						
						try {
							intent.putExtra("id", jo.getInt("requesterId"));
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
	
	public void editPost(View view){
		
		Intent intent = new Intent(this, PostOrEditBookActivity.class);
		
		intent.putExtra("id", id);
		intent.putExtra("isPost", false);
		intent.putExtra("name", name.getText().toString());
		intent.putExtra("price", price.getText().toString()
				.substring(0, price.getText().toString().indexOf(' ')));
		intent.putExtra("sOrR", sOrR);
		if(!sOrR) {
			intent.putExtra("per", per);
			intent.putExtra("duration", duration);
		}
		intent.putExtra("description", description);
		
//		Drawable drawable = cover.getDrawable();
//		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
//		Bitmap bitmap = bitmapDrawable.getBitmap();
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] bytes = coverString.getBytes(Charset.forName("ISO-8859-1")); 
        intent.putExtra("BMP", bytes);
		
		startActivity(intent);
	}
	
	public void deletePost(View view){
		//TODO
	}
}