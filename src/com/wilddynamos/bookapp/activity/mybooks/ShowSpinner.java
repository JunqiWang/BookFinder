package com.wilddynamos.bookapp.activity.mybooks;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.wilddynamos.bookapp.R;


public class ShowSpinner{
	private Spinner spinner;
	private static final String[] unitSelection={"day","week","year"};
	ShowSpinner(){}
	ShowSpinner(Spinner spinner){
		this.spinner = spinner;
	}
	public void setSpinner(Activity activity){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.mybooks_spinneritem,unitSelection);
		spinner.setAdapter(adapter);
		
	}
	public void setListener(){
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
	    {
	        @Override
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	        	// TODO Auto-generated method stub
	        	
	        	arg0.setVisibility(View.VISIBLE);
	        	}
	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {
	        	// TODO Auto-generated method stub                     
	        }
	      }); 
	}
}