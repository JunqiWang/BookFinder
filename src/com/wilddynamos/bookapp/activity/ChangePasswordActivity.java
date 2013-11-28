package com.wilddynamos.bookapp.activity;



import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.wilddynamos.bookapp.R;

public class ChangePasswordActivity extends Activity {
	
	private EditText oldPassword;
	private EditText newPassword;
	private EditText confirmation;
	private Button save;
	private Button cancel;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmation = (EditText) findViewById(R.id.changepassword_confirm);
        save = (Button) findViewById(R.id.chagePassword_saveButton);
        cancel = (Button) findViewById(R.id.chagePassword_cancelButton);
        
    }
}
