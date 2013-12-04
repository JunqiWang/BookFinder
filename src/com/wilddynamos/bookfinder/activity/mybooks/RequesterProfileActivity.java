package com.wilddynamos.bookfinder.activity.mybooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.activity.BaseProfileActivity;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.AcceptRequest;
import com.wilddynamos.bookfinder.ws.remote.action.mybooks.DeclineRequest;
import com.wilddynamos.bookfinder.ws.remote.action.profile.GetProfile;

public class RequesterProfileActivity extends BaseProfileActivity {

	private int id, bookId;

	@Override
	protected void createFunctionSpecificView() {
		if (!getIntent().getExtras().getBoolean("responded"))
			((LinearLayout) findViewById(R.id.profile_requester))
					.setVisibility(LinearLayout.VISIBLE);

		id = getIntent().getExtras().getInt("id");
		bookId = getIntent().getExtras().getInt("bookId");
		new GetProfile(this).execute(new String[] { id + "", bookId + "" });
	}

	public void acceptRequest(View view) {
		new AlertDialog.Builder(this)
				.setTitle("Accept this request?")
				.setMessage(
						"Caution: This will automatically decline all other requesters.")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new AcceptRequest(RequesterProfileActivity.this)
										.execute(new String[] { bookId + "",
												id + "" });
							}
						}).setNegativeButton("No", null).show();
	}

	public void declineRequest(View view) {
		new AlertDialog.Builder(this)
				.setTitle("Decline this request?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new DeclineRequest(RequesterProfileActivity.this)
										.execute(new String[] { bookId + "",
												id + "" });
							}
						})
				.setNegativeButton("No", null)
				.show();
	}
}
