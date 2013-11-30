package com.wilddynamos.bookapp.activity.mybooks;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.mybooks.AcceptRequest;
 
public class LazyAdapter extends BaseAdapter {
 
    private RequesterListActivity activity;
    private ArrayList<HashMap<String,String>> data;
    private static LayoutInflater inflater = null;
    private List<Integer> ids;
    
    public LazyAdapter(RequesterListActivity a, ArrayList<HashMap<String,String>> d, List<Integer> ids) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ids = ids;
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
            vi = inflater.inflate(R.layout.mybooks_requestlist_item, null);
 
        TextView requesterName = (TextView)vi.findViewById(R.id.requesterName); // title
        ImageView profileImage = (ImageView)vi.findViewById(R.id.mybooks_requester_image);
        Button viewButton = (Button)vi.findViewById(R.id.viewButton);
        Button acceptButton = (Button)vi.findViewById(R.id.acceptButton);
        
        final int pos = position;
        acceptButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AcceptRequest ar = new AcceptRequest(activity);
				String[] params = {String.valueOf(activity.getBookId()), 
						String.valueOf(activity.getIds().get(pos))};
				ar.execute(params);
			}
		});	
        
        viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
            	showRequester(pos);
			}
		});	
        
        
        HashMap<String, String> requester = new HashMap<String, String>();
        requester = data.get(position);
 
        // Setting all values in listview
        requesterName.setText(requester.get("name"));
        String s = requester.get("photo");
        if(s != null && !"".equals(s)) {
			byte[] image = s.getBytes(Charset.forName("ISO-8859-1"));
			Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
			profileImage.setImageBitmap(bitmap);
		}
        
        return vi;
    }
    
    public void showRequester(int position) {
		Intent intent = new Intent(activity, RequesterDetailActivity.class); 
		intent.putExtra("id", ids.get(position));
		activity.startActivity(intent);
	}
}