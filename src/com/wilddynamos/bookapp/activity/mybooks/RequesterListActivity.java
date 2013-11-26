package com.wilddynamos.bookapp.activity.mybooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.post.PostDetailsActivity;
import com.wilddynamos.bookapp.activity.post.PostListActivity;
import com.wilddynamos.bookapp.activity.post.PostListActivity.PostListAdapter;
import com.wilddynamos.bookapp.ws.remote.action.mybooks.GetMyRequestList;
import com.wilddynamos.bookapp.ws.remote.action.post.GetPostList;
 
public class RequesterListActivity extends Activity
		implements SensorEventListener, OnTouchListener	{
	
    // All static variables
    private SensorManager mSensorManager = null;
    private ProgressBar refreshProgress,
						loadProgress;
    
    private ListView requesterList;
    private Button declineButton;
    private LazyAdapter adapter;
    
    List<Map<String, String>> list;
    private float yDown;
	private float touchSlop;
	private boolean willRefresh = false;
	private boolean willLoad = false;
	private boolean atTop = true;
	private boolean atBottom = false;
	
    private List<Integer> ids;
    private int currentPage;
    private Thread load;
    
    private Handler handler = new Handler() {

		@Override
    	public void handleMessage(Message msg){
    		if(msg.what == -1)
    			Toast.makeText(RequesterListActivity.this, "Oops!", Toast.LENGTH_SHORT).show();
    		else if(msg.what == 1) {
    			if(currentPage == 1)
    				pour();
    			else {
    				//loadData();
    				//pla.notifyDataSetChanged();
    				loadProgress.setVisibility(ProgressBar.INVISIBLE);
    			}
    		} else
    			Toast.makeText(RequesterListActivity.this, "What happened?", Toast.LENGTH_SHORT).show();
    	}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_requestlist);
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        requesterList = (ListView) findViewById(R.id.postlist);
        refreshProgress = (ProgressBar) findViewById(R.id.refreshProgress);
		loadProgress = (ProgressBar) findViewById(R.id.loadProgress);
		declineButton = (Button) findViewById(R.id.declineButton);
		
		
		list = new ArrayList<Map<String, String>>();
		ids = new ArrayList<Integer>();
		
		refresh();
		
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
//        ArrayList<HashMap<String, String>> requestList = new ArrayList<HashMap<String, String>>();
//        
//        HashMap<String, String> map = new HashMap<String, String>();
//      
//        map.put(KEY_REQUEST_NAME, "Jun qi wang");
//        requestList.add(map);
//       
//        map = new HashMap<String, String>();
//        map.put(KEY_REQUEST_NAME, "Zhe Qian");
//        requestList.add(map);
//        
//        list = (ListView)findViewById(R.id.request_list);
// 
//        // Getting adapter by passing data ArrayList
//        adapter = new LazyAdapter(this, requestList);
//        list.setAdapter(adapter);
// 
//        // Click event for single list row
//        list.setOnItemClickListener(new OnItemClickListener() {
// 
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                    int position, long id) {
// 
//            }
//        });
    }
    
    public void showRequester(int position) {
		Intent intent = new Intent(this, PostDetailsActivity.class); //need another activity
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
	
	@SuppressWarnings("deprecation")
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
					load.stop();
				} catch(Exception e) {
				}
				load = new GetMyRequestList(RequesterListActivity.this);
				load.start();
			}
			break;
		}
		
		return false;
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	//private methods
    @SuppressWarnings("deprecation")
	private void refresh() {
		requesterList.setTop(50);
		refreshProgress.setVisibility(ProgressBar.VISIBLE);
		
		currentPage = 1;
		
		try {
			load.stop();
		} catch(Exception e) {
		}
		load = new GetMyRequestList(RequesterListActivity.this);
		load.start();
	}
    
    private void pour() {
		list.clear();
		loadData();
		
		pla = new LAdapter(
						this,
						list, R.layout.post_postitem,
						new String[] {"name", "price", "likes"},  
						new int[] {R.id.bookNamePostList, R.id.bookPricePostList, R.id.likeNumPostList});
		
		requesterList.setAdapter(pla);
		
		mSensorManager
				.registerListener(
						this, 
						mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
						SensorManager.SENSOR_DELAY_NORMAL);
		
		refreshProgress.setVisibility(ProgressBar.INVISIBLE);
		requesterList.setTop(0);
	}
    
}