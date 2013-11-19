package com.wilddynamos.bookapp.activity;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.Login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity {
	
	private EditText email;
	private EditText password;
	private Button login;
	private Button signup;
	private Button forgotPwd;
	
	private Handler handler = new Handler() {
    	@Override
    	public void handleMessage(Message msg){
    		
    		if(msg.what == -1)
    			Toast.makeText(ForgotPasswordActivity.this, "Failed...", Toast.LENGTH_LONG).show();
    		else if(msg.what == 1) {
    			Toast.makeText(ForgotPasswordActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
    	        signIn();
    		} else
    			Toast.makeText(ForgotPasswordActivity.this, "What happened?", Toast.LENGTH_LONG).show();
    	}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        email = (EditText) findViewById(R.id.userInputEmail);
        password = (EditText) findViewById(R.id.userInputPassword);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        forgotPwd = (Button) findViewById(R.id.forgotPwd);
        
        login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				new Login(ForgotPasswordActivity.this,
//						  email.getEditableText().toString(),
//						  password.getEditableText().toString())
//					.start();
////				signIn();
			}
		});
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    
    public void signIn(){
    	Intent intent = new Intent(this, MultiWindowActivity.class);	
    	startActivity(intent);
    }
    
    public void signUp(View view){
    	Intent intent = new Intent(this, SignupActivity.class);
    	startActivity(intent);
    }
    
    public void forgotPassword(View view) {
    	Intent intent = new Intent(this, SignupActivity.class);
    	startActivity(intent);
    }
    
    public Handler getHandler() {
    	return handler;
    }
}
