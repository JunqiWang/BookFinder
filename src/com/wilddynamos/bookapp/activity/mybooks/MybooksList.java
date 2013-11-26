package com.wilddynamos.bookapp.activity.mybooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wilddynamos.bookapp.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SearchView;

public class MybooksList extends Activity {
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybooks_mybookslist);
		
		// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
        
      
        
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        
        // setting list adapter
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
//		ArrayAdapter adapter = new ArrayAdapter<String>(this, 
//		android.R.layout.simple_list_item_1, new String[]{"abc", "def", "ghi"});
//		ListView listView = (ListView) findViewById(R.id.postlist);
//		listView.setBackgroundColor(0);
//		listView.setAdapter(adapter);
        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandablelistview,
                    View clickedView, int groupPosition, int childPosition, long childId) {
            	switch (groupPosition){
            	case 0:
            		showBookForSaleInfo(clickedView);
            		break;
            	case 1:
            		showBookForRentInfo(clickedView);
            		break;
            	case 2:
            		showBuyRequestDetails(clickedView);
            		break;
            	case 3:
            		showBorrowRequestDetails(clickedView);
            		break;
            	default:
            		break;
            	}
            	
                return false;
            }
        });
	}
	
	
	private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Sell");
        listDataHeader.add("Rent");
        listDataHeader.add("Buy");
        listDataHeader.add("Borrow");
 
        // Adding child data
        List<String> sell = new ArrayList<String>();
        sell.add("The Shawshank Redemption");
        sell.add("The Godfather");
        sell.add("The Godfather: Part II");
        sell.add("Pulp Fiction");
        sell.add("The Good, the Bad and the Ugly");
        sell.add("The Dark Knight");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
//        sell.add("12 Angry Men");
        sell.add("12 Angry Men");
 
        List<String> rent = new ArrayList<String>();
        rent.add("The Conjuring");
        rent.add("Despicable Me 2");
        rent.add("Turbo");
        rent.add("Grown Ups 2");
        rent.add("Red 2");
        rent.add("The Wolverine");
 
        List<String> buy = new ArrayList<String>();
        buy.add("2 Guns");
        buy.add("The Smurfs 2");
        buy.add("The Spectacular Now");
        buy.add("The Canyons");
        buy.add("Europa Report");
        
        List<String> borrow = new ArrayList<String>();
        borrow.add("2 Guns");
        borrow.add("The Smurfs 2");
        borrow.add("The Spectacular Now");
        borrow.add("The Canyons");
        borrow.add("Europa Report");
 
        listDataChild.put(listDataHeader.get(0), sell); // Header, Child data
        listDataChild.put(listDataHeader.get(1), rent);
        listDataChild.put(listDataHeader.get(2), buy);
        listDataChild.put(listDataHeader.get(3), borrow);
        
       
    }


//	@SuppressLint("NewApi")
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		MenuItem searchItem = menu.findItem(R.id.action_search);
//	    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//	    
//		return true;
//	}
	
	public void showBookForRentInfo(View view){
		Intent intent = new Intent(this, MyPostDetailActivity.class);
		intent.putExtra("id", 2);//TODO
		startActivity(intent);
	}
	
	public void showBookForSaleInfo(View view){
		Intent intent = new Intent(this, MyPostDetailActivity.class);
		intent.putExtra("id", 1);//TODO
		startActivity(intent);
	}
	
	public void showBorrowRequestDetails(View view){
		Intent intent = new Intent(this, MyRequestedDetailActivity.class);
		intent.putExtra("id", 2);//TODO
		startActivity(intent);
	}
	
	public void showBuyRequestDetails(View view){
		Intent intent = new Intent(this, MyRequestedDetailActivity.class);
		intent.putExtra("id", 2);//TODO
		startActivity(intent);
	}

}
