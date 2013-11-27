package com.wilddynamos.bookapp.activity.post;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.post.GetPostList;

public class PostListActivity extends Activity 
			implements SensorEventListener, OnTouchListener {
	
	private SensorManager mSensorManager = null;
	
	private EditText searchContent;
	private CheckBox rent, 
					 sell;
	private ListView bookList;
	private ProgressBar refreshProgress,
						loadProgress;
	
	private PostListAdapter pla;
	private List<Map<String, String>> list;
	
	private float yDown;
	private float touchSlop;
	private boolean willRefresh = false;
	private boolean willLoad = false;
	private boolean atTop = true;
	private boolean atBottom = false;
	
	private List<Integer> ids;
	private int currentPage;
	private String sOrR = null;
	private String search = "";
	
	private GetPostList load;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_list);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		searchContent = (EditText) findViewById(R.id.searchContent);
		rent = (CheckBox) findViewById(R.id.rentCheckBox);
		sell = (CheckBox) findViewById(R.id.sellCheckBox);
		bookList = (ListView) findViewById(R.id.postlist);
		refreshProgress = (ProgressBar) findViewById(R.id.refreshProgress);
		loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
		
		touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
		
		list = new ArrayList<Map<String, String>>();
		ids = new ArrayList<Integer>();
		
		refresh();
		
		rent.setOnCheckedChangeListener(new CheckBoxListener());
		sell.setOnCheckedChangeListener(new CheckBoxListener());
		
		bookList.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	show(position);
            }
		});
		
		bookList.setOnTouchListener(this);
		bookList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem == 0)
					atTop = true;
				else
					atTop = false;
				
				if(firstVisibleItem + visibleItemCount == totalItemCount 
						&& firstVisibleItem != 0)
					atBottom = true;
				else
					atBottom = false;
			}
		});
	}
	
	private class CheckBoxListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(!rent.isChecked() && buttonView.equals(sell))
				sell.setChecked(true);
			else if(!sell.isChecked() && buttonView.equals(rent))
				rent.setChecked(true);
			else {
				if(buttonView.equals(sell))
					sOrR = sell.isChecked() ? null : "r";
				else if(buttonView.equals(rent))
					sOrR = rent.isChecked() ? null : "s";
				else
					sOrR = null;
				
				refresh();
			}
		}
		
	}
	
	private void refresh() {
		bookList.setTop(50);
		refreshProgress.setVisibility(ProgressBar.VISIBLE);
		
		currentPage = 1;
		
		try {
			load.cancel(true);
		} catch(Exception e) {
		}
		load = new GetPostList(this);
		load.execute(new String[]{currentPage + "", sOrR, search});
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			float[] values = event.values;
			
		    if (Math.abs(values[0]) > 16 || Math.abs(values[1]) > 16 || Math.abs(values[2]) > 16){
		    	
				mSensorManager
						.unregisterListener(this, 
								mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
				
				refresh();
		    }
		}
	}
	
	public void loadData(JSONArray jsonArray) {
		if(jsonArray == null)
			return;
		
		try {
			for(int i = 0; i < jsonArray.length(); i ++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				
				ids.add(jo.getInt("id"));
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("name", jo.getString("name"));
				map.put("likes", jo.getString("likes"));
				
				if(jo.getBoolean("sOrR"))
					map.put("price", jo.getString("price") + " (s)");
				else
					map.put("price", jo.getString("price") + jo.getString("per") + " (r)");
				
				list.add(map);
			}
			
		} catch (JSONException e) {
		}
		
		if(jsonArray.length() > 0)
			currentPage ++;
		
		if(pla != null)
			pla.notifyDataSetChanged();
		loadProgress.setVisibility(ProgressBar.INVISIBLE);
	}
	
	public void pour(JSONArray jsonArray) {
		ids.clear();
		list.clear();
		loadData(jsonArray);
		
		pla = new PostListAdapter(
						this,
						list, R.layout.post_item,
						new String[] {"name", "price", "likes"},  
						new int[] {R.id.bookNamePostList, R.id.bookPricePostList, R.id.likeNumPostList});
		
		bookList.setAdapter(pla);
		
		refreshProgress.setVisibility(ProgressBar.INVISIBLE);
		bookList.setTop(0);
		
		mSensorManager
			.registerListener(
				this, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	private class PostListAdapter extends SimpleAdapter {
		private int[] colors = {R.color.gray, R.color.white};
		
		public PostListAdapter(Context context,
						List<? extends Map<String, ?>> books, int resource,
						String[] from, int[] to) {
			
			super(context, books, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = super.getView(position, convertView, parent);
			view.setBackgroundResource(colors[position % 2]);
			
			return view;
		}
	}
	
	public void searchPost(View view) {
		search = searchContent.getText().toString();
		refresh();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			yDown = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float yMove = event.getRawY();
			float distance = yMove - yDown;
			
			if(Math.abs(distance) < touchSlop)
				return false;
			else if(distance < 0 && atBottom)
				willLoad = true;
			else if(distance > 0 && atTop)
				willRefresh = true;
			
			break;
		case MotionEvent.ACTION_UP:
			if(willRefresh) {
				willRefresh = false;
				refresh();
			}
			if(willLoad) {
				willLoad = false;
				loadProgress.setVisibility(ProgressBar.VISIBLE);
				
				try {
					load.cancel(true);
				} catch(Exception e) {
				}
				load = new GetPostList(this);
				load.execute(new String[]{currentPage + "", sOrR, search});
			}
			break;
		}
		
		return false;
	}
	
	public void show(int position) {
		Intent intent = new Intent(this, PostDetailActivity.class);
		intent.putExtra("id", ids.get(position));
		startActivity(intent);
	}
}
