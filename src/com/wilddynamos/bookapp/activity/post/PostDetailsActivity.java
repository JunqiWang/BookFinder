package com.wilddynamos.bookapp.activity.post;

import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.post.GetPostDetail;
import com.wilddynamos.bookapp.ws.remote.action.post.LikeAPost;
import com.wilddynamos.bookapp.ws.remote.action.post.RequestBook;

public class PostDetailsActivity extends Activity {
	
	ImageView bg;
	TextView verb;
	TextView name;
	TextView price;
	ImageView cover;
	TextView availableTime;
	TextView owner;
	TextView description;
	ImageView likeIt;
	TextView likes;
	Button request;
	
	EditText message;
	
	int id;
	
	private Handler handler = new Handler() {
		@Override
    	public void handleMessage(Message msg){
    		
    		if(msg.what < 0)
    			Toast.makeText(PostDetailsActivity.this, "Oops!", Toast.LENGTH_LONG).show();
    		else if(msg.what == 1)
    			fill();
    		else if(msg.what == 2) {
    			String s = (Integer.parseInt(likes.getText().toString()) + 1) + "";
    			likes.setText(s);
    		} else if(msg.what == 3)
    			Toast.makeText(PostDetailsActivity.this, "Your request has been sent", Toast.LENGTH_LONG).show();
    		else
    			Toast.makeText(PostDetailsActivity.this, "What happened?", Toast.LENGTH_LONG).show();
    	}
	};
	
	private JSONArray jsonArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_bookdetails);
		
		bg = (ImageView) findViewById(R.id.bookdetails_background11);
		bg.setAlpha(1f);
		
		verb = (TextView) findViewById(R.id.bookdetails_titleBookDetail);
		name = (TextView) findViewById(R.id.bookdetails_nameBookDetail);
		price = (TextView) findViewById(R.id.bookdetails_priceBookDetail);
		cover = (ImageView) findViewById(R.id.bookdetails_coverBookDetail);
		availableTime = (TextView) findViewById(R.id.bookdetails_availableBookDetail);
		owner = (TextView) findViewById(R.id.bookdetails_ownerBookDetail);
		description = (TextView) findViewById(R.id.bookdetails_descriptionBookDetail);
		likeIt = (ImageView) findViewById(R.id.bookdetails_likeBookDetail);
		likes  = (TextView) findViewById(R.id.bookdetails_likeNumBookDetail);
		request = (Button) findViewById(R.id.bookdetails_request);
		
		id = getIntent().getExtras().getInt("id");
		
		new GetPostDetail(this, id).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private DialogInterface.OnClickListener messageListener
		= new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which) {
			case DialogInterface.BUTTON_POSITIVE:
				new RequestBook(PostDetailsActivity.this, id, message.getText().toString()).start();
				request.setText("Has Requested");
				request.setClickable(false);
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				new RequestBook(PostDetailsActivity.this, id, null).start();
				break;
			default:
				new RequestBook(PostDetailsActivity.this, id, message.getText().toString()).start();
			}
		}
		
	};
	
	@SuppressLint("NewApi")
	private void fill() {
		if(jsonArray == null || jsonArray.length() == 0)
			return;
		
		Boolean hasRequested = false;
		String s = null;
		try {
			JSONObject jo = jsonArray.getJSONObject(0);
			
			name.setText(jo.getString("name"));
			owner.setText(jo.getString("owner"));
			description.setText(jo.getString("description"));
			likes.setText(jo.getString("likes"));
			
			if(jo.getBoolean("sOrR")) {
				verb.setText("Buy");
				price.setText(jo.getString("price"));
				request.setText("Buy");
			} else {
				verb.setText("Borrow");
				price.setText(jo.getString("price") 
						+ " / " + (jo.getBoolean("per") ? "week" : "month"));
				availableTime.setText("Available for " 
						+ jo.getInt("availableTime") + (jo.getBoolean("per") ? " weeks" : " months"));
				request.setText("Borrow");
			}
			s = jo.getString("cover");
			hasRequested = jo.getBoolean("hasRequested");
			
		} catch (JSONException e) {
		}
		byte[] cover = s.getBytes(Charset.forName("ISO-8859-1"));
		Bitmap bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.length);
		this.cover.setImageBitmap(bitmap);
		
		if(hasRequested) {
			request.setText("Has Requested");
			request.setClickable(false);
		} else {
			request.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(PostDetailsActivity.this)
						.setTitle(verb.getText() + " this book?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								new AlertDialog.Builder(PostDetailsActivity.this)
									.setTitle("Leave a message to owner?")
									.setView(message = new EditText(PostDetailsActivity.this))
									.setPositiveButton("Send", messageListener)
									.setNegativeButton("Skip", messageListener)
									.show();
	
							}
					})
					.setNegativeButton("No", null).show();
				}
			});
		}
		
		likeIt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LikeAPost(PostDetailsActivity.this, id).start();
			}
		});
		
		bg.setAlpha(0.3f);
	}

	public Handler getHandler() {
		return handler;
	}
	
	public void setJSONArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

}
