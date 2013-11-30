package com.wilddynamos.bookapp.activity.mybooks;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.BaseProfileActivity;
import com.wilddynamos.bookapp.ws.remote.action.mybooks.AcceptRequest;
import com.wilddynamos.bookapp.ws.remote.action.profile.GetProfile;

public class RequesterProfileActivity extends BaseProfileActivity {

	private int id, bookId;

	@Override
	protected void createFunctionSpecificView() {
		if(!getIntent().getExtras().getBoolean("responded"))
			((LinearLayout) findViewById(R.id.profile_requester))
					.setVisibility(LinearLayout.VISIBLE);

		id = getIntent().getExtras().getInt("id");
		bookId = getIntent().getExtras().getInt("bookId");
		new GetProfile(this).execute(new String[] { id + "" });
	}

	public void acceptRequest(View view) {
		new AcceptRequest(this).execute(new String[] { bookId + "", id + "" });
	}

	public void declineRequest(View view) {
//TODO 
	}
}
