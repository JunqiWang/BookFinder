package com.wilddynamos.bookapp.activity.mybooks;


import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.BaseBookDetailActivity;

public class MyRequestDetailActivity extends BaseBookDetailActivity {
	
	private TextView owner;
	private TextView hasResponded;
	private Button withdraw;
	
	@Override
	protected void createFunctionSpecificView() {
		hasResponded = ((TextView) findViewById(R.id.bookDetail_myRequest_hasResponded));
		withdraw = ((Button) findViewById(R.id.bookDetail_myRequest_withdraw));
		((LinearLayout) findViewById(R.id.bookDetail_ownerLine))
				.setVisibility(LinearLayout.VISIBLE);
		owner = (TextView) findViewById(R.id.bookDetail_owner);
	}
	
	@Override
	protected String[] getParams() {
		return new String[]{id + "", "isRequested"};
	}

	@Override
	protected void fillFunctionSpecificView(JSONObject jo) {
		
		try {
			owner.setText(jo.getString("owner"));
			
			if(!jo.getBoolean("hasResponded")) {
				hasResponded.setText("(Owner has not responded.)");
				hasResponded.setVisibility(TextView.VISIBLE);
				withdraw.setVisibility(Button.VISIBLE);
				
				if(jo.getBoolean("sOrR")) {
					title.setText("My request to buy");
				} else {
					title.setText("My request to rent");
				}
				
			} else {
				;//TODO
				if(jo.getBoolean("sOrR")) {
					title.setText("Have bought this book.");
				} else {
					title.setText("Using this book");
				}
			}
			
		} catch (JSONException e) {
		}
		
	}
	
	public void withdraw(View view){
		//TODO
	}
}
