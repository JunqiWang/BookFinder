package com.wilddynamos.bookfinder.activity;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.activity.mybooks.MyBookListActivity;
import com.wilddynamos.bookfinder.activity.post.PostListActivity;
import com.wilddynamos.bookfinder.activity.profile.MyProfileActivity;
import com.wilddynamos.bookfinder.service.NotificationCenter;

/** Activity for multiwindow display **/
@SuppressWarnings("deprecation")
public class MultiWindowActivity extends TabActivity {
	public final static String TAB_SELECT = "tab_select";
	Menu myMenu;
	TabHost tabHost;

	/** Called when the activity is first created. **/
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiwindow);
		System.out.println(0);
		// create the TabHost that will contain the Tabs
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		TabSpec tab1 = tabHost.newTabSpec("First Tab");
		TabSpec tab2 = tabHost.newTabSpec("Second Tab");
		TabSpec tab3 = tabHost.newTabSpec("Third tab");

		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected

		tab1.setIndicator("Posts");
		tab1.setContent(new Intent(this, PostListActivity.class));

		tab2.setIndicator("My Books");
		tab2.setContent(new Intent(this, MyBookListActivity.class));

		tab3.setIndicator("Profile");
		tab3.setContent(new Intent(this, MyProfileActivity.class));

		int tab_num = getIntent().getIntExtra(TAB_SELECT, 0);

		getIntent().putExtra(TAB_SELECT, 0);
		/** Add the tabs to the TabHost to display. */
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);
		tabHost.setCurrentTab(tab_num);
		updateTab(tabHost);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String arg0) {
				tabHost.setCurrentTabByTag(arg0);
				updateTab(tabHost);
			}
		});
		startService(new Intent(this, NotificationCenter.class));
	}

	/** update color and text in tabHost **/
	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
				// on a large screen device ...
				tv.setTextSize(18);
			} else {
				// on a normal screen device
				tv.setTextSize(14);
			}

			if (tabHost.getCurrentTab() == i) {
				// when selected
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_selected));
				tv.setTextColor(Color.WHITE);
			} else {
				// when not selected
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tab_unselected));
				tv.setTextColor(Color.WHITE);
			}
		}
	}

}
