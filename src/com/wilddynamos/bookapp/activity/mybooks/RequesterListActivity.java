package com.wilddynamos.bookapp.activity.mybooks;

import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.wilddynamos.bookapp.R;
 
public class RequesterListActivity extends Activity {
    // All static variables

    static final String KEY_REQUEST_NAME = "requester";
    static final String KEY_REQUESTER_IMAGE = "requester_image";
 
    ListView list;
    LazyAdapter adapter;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_requestlist);
 
        ArrayList<HashMap<String, String>> requestList = new ArrayList<HashMap<String, String>>();
        
        HashMap<String, String> map = new HashMap<String, String>();
      
        map.put(KEY_REQUEST_NAME, "Jun qi wang");
        requestList.add(map);
       
        map = new HashMap<String, String>();
        map.put(KEY_REQUEST_NAME, "Zhe Qian");
        requestList.add(map);
        
        list = (ListView)findViewById(R.id.request_list);
 
        // Getting adapter by passing data ArrayList
        adapter = new LazyAdapter(this, requestList);
        list.setAdapter(adapter);
 
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
 
            }
        });
    }
}