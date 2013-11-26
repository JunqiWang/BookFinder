package com.wilddynamos.bookapp.activity;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.Login;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private EditText email;
	private EditText password;
	private Button login;

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
<<<<<<< HEAD
						&& !"".equals(password.getText().toString()))
					new Login(LoginActivity.this,
							  email.getText().toString(),
							  password.getText().toString())
						.start();  
			//	signIn();
=======
						&& !"".equals(password.getText().toString())) {
					
					Login login = new Login(LoginActivity.this);
					login.execute(new String[]{email.getText().toString(), 
											   password.getText().toString()}
								 );
				}
>>>>>>> 5b1caf5be9f06ac38f5fb113407275337e1bd8d0
			}
		});
    }

    public void signIn(){
    	Intent intent = new Intent(this, MultiWindowActivity.class);
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
    
}
