package com.wilddynamos.bookapp.activity;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.profile.EditProfileActivity;
import com.wilddynamos.bookapp.ws.remote.action.Login;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private EditText email;
	private EditText password;
	private Button login;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
    	@Override
    	public void handleMessage(Message msg){
    		
    		if(msg.what == -1)
    			Toast.makeText(LoginActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
    		else if(msg.what == 1) {
    			Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
    	        signIn();
    		} else
    			Toast.makeText(LoginActivity.this, "What happened?", Toast.LENGTH_SHORT).show();
    	}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        email = (EditText) findViewById(R.id.userInputEmail);
        password = (EditText) findViewById(R.id.userInputPassword);
        login = (Button) findViewById(R.id.login);
        
        login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!"".equals(email.getText().toString()) 
						&& !"".equals(password.getText().toString()))
					new Login(LoginActivity.this,
							  email.getText().toString(),
							  password.getText().toString())
						.start(); 
				signIn();
			}
		});
    }

    public void signIn(){
    	Intent intent = new Intent(this, EditProfileActivity.class);	
    	startActivity(intent);
    }
    
    public void signUp(View view){
    	Intent intent = new Intent(this, SignupActivity.class);
    	startActivity(intent);
    }
    
    public void forgotPassword(View view) {
    	Intent intent = new Intent(this, ForgotPasswordActivity.class);
    	startActivity(intent);
    }
    
    public Handler getHandler() {
    	return handler;
    }
}
