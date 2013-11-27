package com.wilddynamos.bookapp.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.ws.remote.action.Signup;

public class SignupActivity extends Activity {
	
	private EditText email;
	private EditText name;
	private EditText password;
	private EditText confirmation;
	private Button signup;
	private Context context;
	
	private String[] params = null;
	
//	private Handler handler = new Handler() {
//    	@Override
//    	public void handleMessage(Message msg){
//    		
//    		if(msg.what == -1)
//    			Toast.makeText(SignupActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
//    		else if(msg.what == -2)
//    			Toast.makeText(SignupActivity.this, "Email exists!", Toast.LENGTH_SHORT).show();
//    		else if (msg.what == 1) {
//    			new Thread() {
//    				@Override
//    				public void run() {
//    					Connection.login(email.getEditableText().toString(), 
//    							password.getEditableText().toString());
//    					UserDataSource userDataSource = new UserDataSource(context);
//    					userDataSource.open();
//    					User user = userDataSource.createUser(Connection.id, email.getEditableText().toString(), 
//    							password.getEditableText().toString(), 
//    							name.getEditableText().toString(), null, null, null, null, null);
//    					userDataSource.close();
//    				}
//    			}.start();
//    			
//    			Toast.makeText(SignupActivity.this, "Welcome, new friend!", Toast.LENGTH_SHORT).show();
//    		} else
//    			Toast.makeText(SignupActivity.this, "What happened?", Toast.LENGTH_SHORT).show();
//    	}
//	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        
        email = (EditText) findViewById(R.id.userEmail);
        name = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.userPassword);
        confirmation = (EditText) findViewById(R.id.userPasswordConfirm);
        
        signup = (Button) findViewById(R.id.signupButton);
        
        context = this;
        
        params[0] = email.getEditableText().toString();
        params[1] = name.getEditableText().toString();
        params[2] = password.getEditableText().toString();
        
        signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (password.getEditableText().toString().equals(confirmation.getEditableText().toString())) {
					Signup su = new Signup(SignupActivity.this, context);
					su.execute(params);
				}
//				new Signup(SignupActivity.this,
//						  email.getEditableText().toString(),
//						  name.getEditableText().toString(),
//						  password.getEditableText().toString(),
//						  context)
//					.start();
				else Toast.makeText(SignupActivity.this, "Password confirmation not match!", Toast.LENGTH_LONG).show();
			}
		});
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    } */
    
}
