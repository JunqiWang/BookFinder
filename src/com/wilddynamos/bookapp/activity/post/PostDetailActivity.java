package com.wilddynamos.bookapp.activity.post;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.BaseBookDetailActivity;
import com.wilddynamos.bookapp.utils.ZoomInOutAction;
import com.wilddynamos.bookapp.ws.remote.action.post.RequestBook;

public class PostDetailActivity extends BaseBookDetailActivity {
	
	private TextView owner;
	private Button request;
	
	private EditText message;
	
	private DialogInterface.OnClickListener messageListener
		= new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which) {
			case DialogInterface.BUTTON_POSITIVE:
				new RequestBook(PostDetailActivity.this)
						.execute(new String[]{id + "", message.getText().toString()});
				request.setText("Has Requested");
				request.setClickable(false);
				break;
			}
		}
	};
	
	@Override
	protected void createFunctionSpecificView() {
		((LinearLayout) findViewById(R.id.bookDetail_ownerLine))
				.setVisibility(LinearLayout.VISIBLE);
		owner = (TextView) findViewById(R.id.bookDetail_owner);
		request = (Button) findViewById(R.id.bookDetail_post_request);
		request.setVisibility(Button.VISIBLE);
	}
	
	@Override
	protected String[] getParams() {
		return new String[]{id + ""};
	}

	@Override
	protected void fillFunctionSpecificView(JSONObject jo) {
		
		Boolean hasRequested = false;
		try {
			owner.setText(jo.getString("owner"));
			
			if(jo.getBoolean("sOrR")) {
				title.setText("Buy this book!");
				request.setText("Buy");
			} else {
				title.setText("Borrow this book!");
				request.setText("Borrow");
			}
			
			hasRequested = jo.getBoolean("hasRequested");
			
		} catch (JSONException e) {
			System.out.println(e.toString());
		}
		
		if(hasRequested) {
			request.setText("Has Requested");
			request.setClickable(false);
		} else {
			request.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(PostDetailActivity.this)
						.setTitle(title.getText().subSequence(0, title.getText().length() - 1) + "?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								message = new EditText(PostDetailActivity.this);
								message.setHeight(100);
								message.setHint("Your can click send directly by not leaving a message.");
								
								new AlertDialog.Builder(PostDetailActivity.this)
									.setTitle("Leave a message to owner?")
									.setView(message)
									.setPositiveButton("Send", messageListener)
									.setNegativeButton("Cancel", messageListener)
									.show();
							}
					})
					.setNegativeButton("No", null).show();
				}
			});
		}
	}

}
