package com.wilddynamos.bookapp.activity.post;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.NotificationCenter;
import com.wilddynamos.bookapp.ws.remote.action.post.GetPostList;

public class PostListActivity extends Activity implements SensorEventListener {
	
	private SensorManager mSensorManager = null;
	private Sensor sensor;
	
	private EditText searchContent;
	private CheckBox rent, sell;
	private ListView bookList;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
    	public void handleMessage(Message msg){
    		
    		if(msg.what == -1)
    			Toast.makeText(PostListActivity.this, "Oops!", Toast.LENGTH_SHORT).show();
    		else if(msg.what == 1)
    			pour();
    		else
    			Toast.makeText(PostListActivity.this, "What happened?", Toast.LENGTH_SHORT).show();
    	}
	};
	
	private JSONArray jsonArray;
	private List<Integer> ids;
	private int currentPage;
	private boolean rentChecked = true, sellChecked = true;
	private String sOrR = null;
	private String search = "";
	
	private Thread refresh;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_postlist);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		
//		searchContent = (EditText) findViewById(R.id.searchContent);
		rent = (CheckBox) findViewById(R.id.rentCheckBox);
		sell = (CheckBox) findViewById(R.id.sellCheckBox);
		bookList = (ListView) findViewById(R.id.postlist);
		
		currentPage = 1;

		try {
			refresh.stop();
		} catch(Exception e) {
		}
		refresh = new GetPostList(this);
		refresh.start();
		
		rent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!sellChecked)
					rent.setChecked(true);
				else {
					rentChecked = isChecked;
					sOrR = rentChecked ? null : "s";
					currentPage = 1;
					
					try {
						refresh.stop();
					} catch(Exception e) {
					}
					refresh = new GetPostList(PostListActivity.this);
					refresh.start();
				}
			}
		});
		
		sell.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!rentChecked)
					sell.setChecked(true);
				else {
					sellChecked = isChecked;
					sOrR = sellChecked ? null : "r";
					currentPage = 1;
					
					try {
						refresh.stop();
					} catch(Exception e) {
					}
					refresh = new GetPostList(PostListActivity.this);
					refresh.start();
				}
			}
		});
		
		startService(new Intent(this, NotificationCenter.class));
	}

////	@SuppressLint("NewApi")
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);		
////		MenuItem searchItem = menu.findItem(R.id.action_search);
////	    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
////	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//	    
//		return true;
//	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d("YAO", "jinle");
        int sensorType = event.sensor.getType();
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER){
            if (Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math.abs(values[2]) > 14){
        		try {
					refresh.stop();
				} catch(Exception e) {
				}
				refresh = new GetPostList(PostListActivity.this);
				refresh.start();
				Toast.makeText(this, "YAOYAOYAO", Toast.LENGTH_LONG).show();
            }
        }
	}
	
	private void pour() {
		if(jsonArray == null || jsonArray.length() == 0)
			return;
		
		ids = new ArrayList<Integer>();
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
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
		
		bookList.setAdapter(new PostListAdapter(
				this,
				list, R.layout.post_postitem,
				new String[] {"name", "price", "likes"},  
				new int[] {R.id.bookNamePostList, R.id.bookPricePostList, R.id.likeNumPostList}));
		
		bookList.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	show(position);
            }
		});
		
		currentPage ++;
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
	
	@SuppressWarnings({ "deprecation" })
	public void searchPost(View view) {
		if(searchContent.getText() == null || searchContent.getText().toString().equals("")) {
			search = "";
			return;
		}
		
		search = searchContent.getText().toString();
		
		try {
			refresh.stop();
		} catch(Exception e) {
		}
		refresh = new GetPostList(this);
		refresh.start();
	}
	
	/**
	 * Go to detail page
	 * @param view
	 */
	public void show(int position) {
		Intent intent = new Intent(this, PostDetailsActivity.class);
		intent.putExtra("id", ids.get(position));
		
		startActivity(intent);
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	public void setJSONArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public String getsOrR() {
		return sOrR;
	}
	
	public String getSearch() {
		return search;
	}
}
