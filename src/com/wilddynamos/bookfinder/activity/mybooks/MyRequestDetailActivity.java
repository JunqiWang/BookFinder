package com.wilddynamos.bookfinder.activity.mybooks;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.activity.BaseBookDetailActivity;
import com.wilddynamos.bookfinder.ws.remote.Connection;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.WithdrawRequest;

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
		return new String[] { id + "", "isRequested" };
	}

	@Override
	protected void fillFunctionSpecificView(JSONObject jo) {

		try {
			owner.setText(jo.getString("owner"));

			if (jo.getInt("hasResponded") == 0) {
				hasResponded.setText("(Owner has not responded.)");
				hasResponded.setVisibility(TextView.VISIBLE);
				withdraw.setVisibility(Button.VISIBLE);

				if (jo.getBoolean("sOrR")) {
					title.setText("My request to buy");
				} else {
					title.setText("My request to rent");
				}

			} else {
				if (jo.getBoolean("sOrR")) {
					if (jo.getInt("hasResponded") == 1)
						title.setText("Have bought this book.");
					else
						title.setText("Your request been declined");
				} else {
					if (jo.getInt("hasResponded") == -1)
						title.setText("Using this book");
					else
						title.setText("Your request been declined");
				}
			}

		} catch (JSONException e) {
		}

	}

	public void withdraw(View view) {
		new AlertDialog.Builder(this)
				.setTitle("Sure to withdraw this request?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new WithdrawRequest(
										MyRequestDetailActivity.this)
										.execute(new String[] {
												String.valueOf(id),
												String.valueOf(Connection.id) });
							}
						})
				.setNegativeButton("No", null)
				.show();
	}
}
