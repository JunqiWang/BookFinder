package com.wilddynamos.bookapp.activity.mybooks;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.wilddynamos.bookapp.R;
 
public class LazyAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater=null;
 
    public LazyAdapter(Activity a, ArrayList<HashMap<String,String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //    imageLoader=  new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.mybooks_requestlist_row, null);
 
        TextView requesterName = (TextView)vi.findViewById(R.id.requesterName); // title
        ImageView profile_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
 
        HashMap<String, String> requester = new HashMap<String, String>();
        requester = data.get(position);
 
        // Setting all values in listview
        requesterName.setText(requester.get("zhe"));
        profile_image.setImageResource(R.drawable.profile);
        return vi;
    }
}