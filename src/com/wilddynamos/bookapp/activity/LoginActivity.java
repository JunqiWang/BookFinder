package com.wilddynamos.bookapp.activity;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.ForgotPwd;
import com.wilddynamos.bookapp.dblayout.RememberMeDbHelper;
import com.wilddynamos.bookapp.dblayout.RememberMeContract.RememberMeColumn;
import com.wilddynamos.bookapp.ws.remote.action.Login;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
        
    private EditText email;
    private EditText password;
    private Button login;
    
    private ForgotPwd forgotPwd;

    private CheckBox rememberme;
    private RememberMeDbHelper mDbHelper;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
  
        email = (EditText) findViewById(R.id.userInputEmail);
        password = (EditText) findViewById(R.id.userInputPassword);
        login = (Button) findViewById(R.id.login);
        rememberme = (CheckBox) findViewById(R.id.checkbox_remember);
        mDbHelper = new RememberMeDbHelper(getApplicationContext());
        
        setRemember();
        
        login.setOnClickListener(new OnClickListener() {
                        
        	@Override
        	public void onClick(View v) {

        		if(!"".equals(email.getText().toString()) 
        				&& !"".equals(password.getText().toString())) {
                                        
        			Login login = new Login(LoginActivity.this);
        			login.execute(new String[]{email.getText().toString(), 
        					password.getText().toString()}
        			); 
        		}  
        	//	signIn();
        	}
       }); 
    }

    public void signIn(){
    	Intent intent = new Intent(this, MultiWindowActivity.class);
    	startActivity(intent); 
    	if(rememberme.isChecked()){
    		remember();
    	}
    	else
    		notRemember();
    }
    
    public void signUp(View view){
    	Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    
    public void forgotPassword(View view) {
    	final EditText email = new EditText(this);
    	forgotPwd = new ForgotPwd(this);
		new AlertDialog.Builder(this)
				.setTitle("Enter your email")
				.setView(email)
				.setPositiveButton("Send", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if("".equals(email.getText().toString()))
							new AlertDialog.Builder(LoginActivity.this)
									.setTitle("You entered nothing.")
									.setPositiveButton("OK", null)
									.show();
						else
							forgotPwd.execute(new String[]{email.getText().toString()});
					}
				})
				.setNegativeButton("Cancel", null)
				.show();
    	Intent intent = new Intent(this, ForgotPasswordActivity.class);
    	startActivity(intent);
    }
    
    public void onCheckboxClicked(View view){
    	if (((CheckBox) view).isChecked()) {
			Toast.makeText(LoginActivity.this,
		 	   "Warning:Your password will be saved", Toast.LENGTH_LONG).show();
		}
    }
    
    public void remember(){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	
    	String myemail = email.getText().toString();
    	String mypassword = password.getText().toString();
   
    	ContentValues values = new ContentValues();
    	values.put(RememberMeColumn.COLUMN_NAME_C1, myemail);
    	values.put(RememberMeColumn.COLUMN_NAME_C2, mypassword);
    	
    	long count = db.insert(
    		RememberMeColumn.TABLE_NAME,
    	    null,
    	    values);
    	db.close();
    }
    
    public void notRemember(){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	db.delete(RememberMeColumn.TABLE_NAME, null, null);
    	db.close();
    }
    
    public void  setRemember(){
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+RememberMeColumn.TABLE_NAME+"'", null);
    	if (cursor.getCount() <= 0 || cursor == null){
    		rememberme.setChecked(false);
    		cursor.close();
    		db.close();
    		return;
    	}
    	String[] projection = {
    			RememberMeColumn.COLUMN_NAME_C1,
    			RememberMeColumn.COLUMN_NAME_C2
    		};
    	
    	String sortOrder = RememberMeColumn._ID + " DESC";

    		cursor = db.query(
    			RememberMeColumn.TABLE_NAME,  			// The table to query
    		    projection,                             // The columns to return
    		    null,                                	// The columns for the WHERE clause
    		    null,                            		// The values for the WHERE clause
    		    null,                                   // don't group the rows
    		    null,                                   // don't filter by row groups
    		    sortOrder                               // The sort order
    		);
    	if (cursor.getCount() <= 0){
    		rememberme.setChecked(false);
    		cursor.close();
    		return;
    	}
    	cursor.moveToFirst();
    		
    	String myemail = cursor.getString(
    			cursor.getColumnIndex(RememberMeColumn.COLUMN_NAME_C1)
    	);
    	String mypassword = cursor.getString(
    			cursor.getColumnIndex(RememberMeColumn.COLUMN_NAME_C2)
    	);
    	rememberme.setChecked(true);
    	email.setText(myemail);
    	password.setText(mypassword);
    	cursor.close();
    	db.close();
    }
}