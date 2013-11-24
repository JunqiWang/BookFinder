package com.wilddynamos.bookapp.activity.profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.ZoomInOutActivity;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.utils.BitmapWorkerTask;
import com.wilddynamos.bookapp.utils.TakePhoto;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.action.profile.EditMyProfile;

public class EditProfileActivity extends Activity {

        private ImageView profileImage;
        private EditText name;
        private EditText gender;
        private EditText campus;
        private EditText contact;
        private EditText address;
        
        private Button takePhoto;
        private Button choosePhoto;
        private Button save;
        private Button cancel;
        
        private User user = new User();
        private Context context;
        
        /***take photo ***/        
        private static final int ACTION_TAKE_PHOTO = 1;
        private static final String BITMAP_STORAGE_KEY = "viewbitmap";
        private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
        public static Bitmap mImageBitmap;
        private String mCurrentPhotoPath;
        
        TakePhoto takePhotoAction;
        /***take photo ***/
        /***choose photo ***/
         final int ACTIVITY_SELECT_IMAGE = 5;
                /***choose photo ***/
        @SuppressLint("HandlerLeak")
        private Handler handler = new Handler() {
    		@Override
        	public void handleMessage(Message msg){
        		
        		if(msg.what == -1)
        			Toast.makeText(EditProfileActivity.this, "Exceptions!", Toast.LENGTH_SHORT).show();
        		else if(msg.what == -2) 
        			Toast.makeText(EditProfileActivity.this, "Both sqlite and mysql wrong!", Toast.LENGTH_SHORT).show();
        		else if(msg.what == -3) 
        			Toast.makeText(EditProfileActivity.this, "Mysql wrong!", Toast.LENGTH_SHORT).show();
        		else if(msg.what == -4) 
        			Toast.makeText(EditProfileActivity.this, "Sqlite wrong!", Toast.LENGTH_SHORT).show();
        		else if(msg.what == 1) {
        			Toast.makeText(EditProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
        			save();
        		}
        		else
        			Toast.makeText(EditProfileActivity.this, "What happened?", Toast.LENGTH_SHORT).show();
        	}
    	};
    	
    	@Override
    	public void onCreate(Bundle savedInstanceState){
    		super.onCreate(savedInstanceState);
            setContentView(R.layout.profile_editprofile);   
            
            profileImage = (ImageView) findViewById(R.id.editprofile_image);
    		name = (EditText) findViewById(R.id.edit_name);
    		gender = (EditText) findViewById(R.id.edit_gender);
    		campus = (EditText) findViewById(R.id.edit_campus);
    		contact = (EditText) findViewById(R.id.edit_contact);
    		address = (EditText) findViewById(R.id.edit_address);
    		takePhoto = (Button) findViewById(R.id.editprofile_take_photo_button);
    		choosePhoto = (Button) findViewById(R.id.editprofile_choose_photo_button);
    		save = (Button) findViewById(R.id.editprofile_saveButton);
    		cancel = (Button) findViewById(R.id.editprofile_cancelButton);
    		
    		context = this;
    		save.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				//deal with the taken photo
    				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    				try{
    					File file = new File(mCurrentPhotoPath);
    					FileInputStream fis = new FileInputStream(file);
    					byte[] buf = new byte[1024 * 1024];
    					for(int readNum; (readNum = fis.read(buf)) != -1;)
    						bos.write(buf, 0, readNum);
    					fis.close();
    				} catch(Exception e) {
    					e.printStackTrace();
    				}
    				byte[] bytes = bos.toByteArray();

    				user.setId(Connection.id);
    				user.setName(name.getEditableText().toString());
    				user.setGender(gender.getEditableText().toString().equals("M"));
    				user.setCampus(campus.getEditableText().toString());
    				user.setContact(contact.getEditableText().toString());
    				user.setAddress(address.getEditableText().toString());
    				user.setPhotoAddr(mCurrentPhotoPath);
    				System.out.println(mCurrentPhotoPath);
    				
    				new EditMyProfile(EditProfileActivity.this, context, user, bytes)
    					.start();
    				
    			}
    		});
                /***take photo ***/
                mImageBitmap = null;
                takePhotoAction = new TakePhoto(this,mCurrentPhotoPath, profileImage, takePhoto);
                takePhotoAction.start();
                /***take photo ***/
                
                /****choose Photo**/
                choosePhoto.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                                // TODO Auto-generated method stub
                                Intent i = new Intent(Intent.ACTION_PICK,
                                   android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);                       
                        startActivityForResult(i, ACTIVITY_SELECT_IMAGE); 
                        }
                });
                /****image zoom in and out***/
                profileImage.setOnClickListener(new OnClickListener() {
        		    public void onClick(View v) {
        		    	zoomInOut(v);
        		    }
        		});
        }
        /* save button*/
        public void save(){
                Intent intent = new Intent(this, MultiWindowActivity.class);
                intent.putExtra(MultiWindowActivity.TAB_SELECT, 2);
                startActivity(intent);
        }
        
        /* cancel button*/
        public void cancel(View view){
                Intent intent = new Intent(this, MultiWindowActivity.class);
                intent.putExtra(MultiWindowActivity.TAB_SELECT, 2);
                startActivity(intent);
        }
        
        public Handler getHandler() {
                return handler;
        }
        /****image zoom in and out***/
        public void zoomInOut(View view){
        	Drawable drawable = profileImage.getDrawable();
    		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
    		mImageBitmap = bitmapDrawable.getBitmap();
    		Intent intent = new Intent(this, ZoomInOutActivity.class);
    		intent.putExtra("BitmapImage", mImageBitmap);
    		startActivity(intent);
    	}
        
        /***take photo ***/
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                switch (requestCode) {
                        /** take photo **/
                        case ACTION_TAKE_PHOTO: {
                                if (resultCode == RESULT_OK) {
                                      //  takePhotoAction.handleCameraPhoto( );
                                	mCurrentPhotoPath = takePhotoAction.getPath();
                                	 BitmapWorkerTask bitmapworker = new BitmapWorkerTask(mCurrentPhotoPath,profileImage);
                                     bitmapworker.execute();
                                     takePhotoAction.galleryAddPic();
                                     mImageBitmap = bitmapworker.getBitmap();
                                }
                                break;
                        }
                        /** choose photo **/
                        case ACTIVITY_SELECT_IMAGE:{
                         if(resultCode == RESULT_OK){  
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            mCurrentPhotoPath = cursor.getString(columnIndex);
                            cursor.close();

                            BitmapWorkerTask bitmapworker = new BitmapWorkerTask(mCurrentPhotoPath,profileImage);
                            bitmapworker.execute();
                            mImageBitmap = bitmapworker.getBitmap();
                        }
                                 break;
                        }
                        default:
                        Toast.makeText(EditProfileActivity.this, "SMS not delivered",
                              Toast.LENGTH_SHORT).show();
                } // switch
        }

        // Some lifecycle callbacks so that the image can survive orientation change

        @Override
        protected void onSaveInstanceState(Bundle outState) {
        		Drawable drawable = profileImage.getDrawable();
        		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        		mImageBitmap = bitmapDrawable.getBitmap();
                outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
                outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
                super.onSaveInstanceState(outState);
        }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
                super.onRestoreInstanceState(savedInstanceState);
                mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
                profileImage.setImageBitmap(mImageBitmap);
                profileImage.setVisibility(
                                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? 
                                                ImageView.VISIBLE : ImageView.INVISIBLE
                );
        }
        /***take photo ***/
} 
