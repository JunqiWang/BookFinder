package com.wilddynamos.bookapp.activity.mybooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.profile.DeclineAll;
import com.wilddynamos.bookapp.ws.remote.action.profile.GetRequestList;
 
public class RequesterListActivity extends Activity
		implements SensorEventListener, OnTouchListener	{
	
    // All static variables
    private SensorManager mSensorManager = null;
    private ProgressBar refreshProgress,
						loadProgress;
    
    private ListView requesterList;
    private Button declineButton;
    private LazyAdapter la;
    
    private int bookId;
    
    ArrayList<HashMap<String, String>> requesterArray;
    private float yDown;
	private float touchSlop;
	private boolean willRefresh = false;
	private boolean willLoad = false;
	private boolean atTop = true;
	private boolean atBottom = false;
	
    private List<Integer> ids;
    private int currentPage;
    
    private JSONArray jsonArray;
    private GetRequestList load;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_requestlist);
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        requesterList = (ListView) findViewById(R.id.postlist);
        refreshProgress = (ProgressBar) findViewById(R.id.refreshProgress);
		loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
		declineButton = (Button) findViewById(R.id.declineButton);
		
		touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
		
		requesterArray = new ArrayList<HashMap<String, String>>();
		ids = new ArrayList<Integer>();
		
		refresh();
		
		declineButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DeclineAll da =  new DeclineAll(RequesterListActivity.this);
				da.execute();
			}
		});	
		
		requesterList.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	showRequester(position);
            }
		});
		
		requesterList.setOnTouchListener(this);
		requesterList.setOnScrollListener(new OnScrollListener() {
			
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
    
    public void showRequester(int position) {
		Intent intent = new Intent(this, RequesterDetailActivity.class); //need another activity
		intent.putExtra("id", ids.get(position));
		startActivity(intent);
	}
    

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			float[] values = event.values;
			
		    if (Math.abs(values[0]) > 16 || Math.abs(values[1]) > 16 || Math.abs(values[2]) > 16){
		    	
		    	refresh();

				mSensorManager
						.unregisterListener(this, 
								mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		    }
		}
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
				GetRequestList load = new GetRequestList(RequesterListActivity.this);
				load.execute(new String[] {String.valueOf(bookId), String.valueOf(currentPage)});
			}
			break;
		}
		
		return false;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public List<Integer> getIds() {
		return ids;
	}
	
	public LazyAdapter getLazyAdapter() {
		return la;
	}
	
	public ProgressBar getLoadProgress() {
		return loadProgress;
	}
	
	public void setJSONArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	
	//private methods
	private void refresh() {
		requesterList.setTop(50);
		refreshProgress.setVisibility(ProgressBar.VISIBLE);
		
		currentPage = 1;
		
		try {
			load.cancel(true);
		} catch(Exception e) {
		}
		GetRequestList load = new GetRequestList(RequesterListActivity.this);
		load.execute(new String[] {String.valueOf(bookId), String.valueOf(currentPage)});
	}
    
    public void pour() {
		requesterArray.clear();
		loadData();
		
		la = new LazyAdapter(this, requesterArray);
		
		requesterList.setAdapter(la);
		
		refreshProgress.setVisibility(ProgressBar.INVISIBLE);
		requesterList.setTop(0);
		
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
						SensorManager.SENSOR_DELAY_NORMAL);
		
	}
    
    public void loadData() {
		if(jsonArray == null)
			return;
		
		try {
			for(int i = 0; i < jsonArray.length(); i ++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				
				ids.add(jo.getInt("id"));
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("name", jo.getString("name"));
				map.put("photo", jo.getString("photo"));
				
				requesterArray.add(map);
			}
			
		} catch (JSONException e) {
		}
		
		if(jsonArray.length() > 0)
			currentPage ++;
	}
    
}