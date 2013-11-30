package com.wilddynamos.bookapp.activity.mybooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.action.mybooks.GetMyBooks;

public class MybooksListActivity extends Activity {
	
	private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    
    private List<Integer> sellIds;
    private List<Integer> rentIds;
    private List<Integer> buyIds;
    private List<Integer> borrowIds;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_mybookslist);
		
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        
        sellIds = new ArrayList<Integer>();
        rentIds = new ArrayList<Integer>();
        buyIds = new ArrayList<Integer>();
        borrowIds = new ArrayList<Integer>();
        
        System.out.println("before load");
        GetMyBooks gmb = new GetMyBooks(this);
        gmb.execute(new String[] {String.valueOf(Connection.id)});
        System.out.println("after load");
        
        // setting list adapter
        
     
	}
	
	public void loadData(JSONArray jsonArray) {
		
		System.out.println("haha");
		listDataHeader = new ArrayList<String>();
      	listDataChild = new HashMap<String, List<String>>();

      	listDataHeader.add("Sell");
      	listDataHeader.add("Rent");
      	listDataHeader.add("Buy");
      	listDataHeader.add("Borrow");
      	System.out.println("hahahahaha");
      	List<String> sell = new ArrayList<String>();
      	List<String> rent = new ArrayList<String>();
      	List<String> buy = new ArrayList<String>();
      	List<String> borrow = new ArrayList<String>();
      	
		if(jsonArray == null)
			return;
		
		try {
			for(int i = 0; i < jsonArray.length(); i ++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				
				//ids.add(jo.getInt("id"));
				int id = jo.getInt("id");
				
				int type = jo.getInt("type");
				Boolean sOrR = jo.getBoolean("sOrR");
				String name = jo.getString("name");
				
				if (type == 1) {
					if (sOrR) {
						sell.add(name);
						sellIds.add(id);
					}
					else {
						rent.add(name);
						rentIds.add(id);
					}
				}
				else if (type == 0) {
					if (sOrR) {
						buy.add(name);
						buyIds.add(id);
					}
					else {
						borrow.add(name); 
						borrowIds.add(id);
					}
				}
			}
			
	        listDataChild.put(listDataHeader.get(0), sell);
	        listDataChild.put(listDataHeader.get(1), rent);
	        listDataChild.put(listDataHeader.get(2), buy);
	        listDataChild.put(listDataHeader.get(3), borrow);
			
		} catch (JSONException e) {
		}
		
	}
	
//	private void loadData(JSONArray jsonArray) {
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<String>>();
// 
//        listDataHeader.add("Sell");
//        listDataHeader.add("Rent");
//        listDataHeader.add("Buy");
//        listDataHeader.add("Borrow");
//        
//        
//        List<String> sell = new ArrayList<String>();
//        sell.add("The Shawshank Redemption");
//        sell.add("The Godfather");
//        sell.add("The Godfather: Part II");
//        sell.add("Pulp Fiction");
//        sell.add("The Good, the Bad and the Ugly");
//        sell.add("The Dark Knight");
//        sell.add("12 Angry Men");
// 
//        List<String> rent = new ArrayList<String>();
//        rent.add("The Conjuring");
//        rent.add("Despicable Me 2");
//        rent.add("Turbo");
//        rent.add("Grown Ups 2");
//        rent.add("Red 2");
//        rent.add("The Wolverine");
// 
//        List<String> buy = new ArrayList<String>();
//        buy.add("2 Guns");
//        buy.add("The Smurfs 2");
//        buy.add("The Spectacular Now");
//        buy.add("The Canyons");
//        buy.add("Europa Report");
//        
//        List<String> borrow = new ArrayList<String>();
//        borrow.add("2 Guns");
//        borrow.add("The Smurfs 2");
//        borrow.add("The Spectacular Now");
//        borrow.add("The Canyons");
//        borrow.add("Europa Report");
// 
//        listDataChild.put(listDataHeader.get(0), sell); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), rent);
//        listDataChild.put(listDataHeader.get(2), buy);
//        listDataChild.put(listDataHeader.get(3), borrow);
//        
//       
//    }
	
	public void showBookForSaleInfo(View view, int childPosition){
		Intent intent = new Intent(this, MyPostDetailActivity.class);
		intent.putExtra("id", sellIds.get(childPosition));
		startActivity(intent);
	}
	
	public void showBookForRentInfo(View view, int childPosition){

		Intent intent = new Intent(this, MyPostDetailActivity.class);
		intent.putExtra("id", rentIds.get(childPosition));
		startActivity(intent);
	}
	
	public void showBuyRequestDetails(View view, int childPosition){
		Intent intent = new Intent(this, MyRequestDetailActivity.class);
		intent.putExtra("id", buyIds.get(childPosition));
		startActivity(intent);
	}
	
	public void showBorrowRequestDetails(View view, int childPosition){
		Intent intent = new Intent(this, MyRequestDetailActivity.class);
		intent.putExtra("id", borrowIds.get(childPosition));
		startActivity(intent);
	}
	
	public void fill() {
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		expListView.setAdapter(listAdapter);
		
		   expListView.setOnGroupExpandListener(new OnGroupExpandListener(){

	        	@Override
	        	public void onGroupExpand(int arg0) {
	        	        // TODO Auto-generated method stub
	        	        for(int i=0;i<listAdapter.getGroupCount();i++)
	        	        {
	        	                if(arg0!=i)
	        	                {
	        	                        expListView.collapseGroup(i);
	        	                }
	        	        }
	        	                                
	        	}
	        	                
	        });
	        expListView.setGroupIndicator(null);
//			ArrayAdapter adapter = new ArrayAdapter<String>(this, 
//			android.R.layout.simple_list_item_1, new String[]{"abc", "def", "ghi"});
//			ListView listView = (ListView) findViewById(R.id.postlist);
//			listView.setBackgroundColor(0);
//			listView.setAdapter(adapter);
	        expListView.setOnChildClickListener(new OnChildClickListener() {
	            @Override
	            public boolean onChildClick(ExpandableListView expandablelistview,
	                    View clickedView, int groupPosition, int childPosition, long childId) {
	            	switch (groupPosition){
	            	case 0:
	            		showBookForSaleInfo(clickedView, childPosition);
	            		break;
	            	case 1:
	            		showBookForRentInfo(clickedView, childPosition);
	            		break;
	            	case 2:
	            		showBuyRequestDetails(clickedView, childPosition);
	            		break;
	            	case 3:
	            		showBorrowRequestDetails(clickedView, childPosition);
	            		break;
	            	default:
	            		break;
	            	}
	            	
	                return false;
	            }
	        });
	}
}
