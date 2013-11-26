package com.wilddynamos.bookapp.activity.profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wilddynamos.bookapp.utils.LocationUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.ZoomInOutActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.utils.BitmapWorkerTask;
import com.wilddynamos.bookapp.utils.TakePhoto;
import com.wilddynamos.bookapp.ws.remote.Connection;
import com.wilddynamos.bookapp.ws.remote.action.profile.EditMyProfile;

public class EditProfileActivity extends Activity implements
	LocationListener,
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener{


        ImageView profileImage;
        ImageView mapImage;
        
        EditText name;
        EditText gender;
        EditText campus;
        EditText contact;
        EditText myaddress;

        
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

        /***choose photo ***/
         final int ACTIVITY_SELECT_IMAGE = 5;
         /***geolocation ***/
         // A request to connect to Location Services
         private LocationRequest mLocationRequest;

         // Stores the current instantiation of the location client in this object
         private LocationClient mLocationClient;

         // Handles to UI widgets

         
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
            
            context = this;
            profileImage = (ImageView) findViewById(R.id.editprofile_image);
    		name = (EditText) findViewById(R.id.edit_name);
    		gender = (EditText) findViewById(R.id.edit_gender);
    		campus = (EditText) findViewById(R.id.edit_campus);
    		contact = (EditText) findViewById(R.id.edit_contact);
    		myaddress = (EditText) findViewById(R.id.edit_address);
    		takePhoto = (Button) findViewById(R.id.editprofile_take_photo_button);
    		choosePhoto = (Button) findViewById(R.id.editprofile_choose_photo_button);
    		save = (Button) findViewById(R.id.editprofile_saveButton);
    		cancel = (Button) findViewById(R.id.editprofile_cancelButton);
    		
    		
    		Thread th = new Thread() {
				@Override
				public void run() {
					UserDataSource userDataSource = new UserDataSource(context);
					userDataSource.open();
					user = userDataSource.getUser(Connection.id);
					userDataSource.close();
				}
			};
    		th.start();
			
			while(th.isAlive());
			
			name.setText(user.getName());
			gender.setText(user.getGender() ? "Male" : "Female");
			campus.setText(user.getCampus());
			contact.setText(user.getContact());
			myaddress.setText(user.getAddress()); 
			
			String photoPath = user.getPhotoAddr();
			if (photoPath != null) {
				System.out.println(photoPath);
				//Bitmap bmp = getBitmap(this, photoPath);
				Bitmap bmp = BitmapFactory.decodeFile(photoPath);
				profileImage.setImageBitmap(bmp);
			}  
			
    		mapImage = (ImageView) findViewById(R.id.edit_map); 
    		
    		/***geolocation***/
 
    	        // Create a new global location parameters object
    	        mLocationRequest = LocationRequest.create();

    	        /*
    	         * Set the update interval
    	         */
    	      //  mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

    	        // Use high accuracy
    	        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    	        // Set the interval ceiling to one minute
    	        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

    	        /*
    	         * Create a new location client, using the enclosing class to
    	         * handle callbacks.
    	         */
    	        mLocationClient = new LocationClient(this, this, this);
    	        
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
    				user.setAddress(myaddress.getEditableText().toString());
    				user.setPhotoAddr(mCurrentPhotoPath);
    				//System.out.println(mCurrentPhotoPath);
    				
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
                /****geolocation***/
                mapImage.setOnClickListener(new OnClickListener() {
        		    public void onClick(View v) {
        		    	getAddress(v);
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
                                	mCurrentPhotoPath = takePhotoAction.getPath();
                                	 BitmapWorkerTask bitmapworker = new BitmapWorkerTask(mCurrentPhotoPath,profileImage);
                                     bitmapworker.execute();
                                     System.out.println(mCurrentPhotoPath);
                                     takePhotoAction.galleryAddPic(mCurrentPhotoPath);
                                    
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
     /***geolocation***/
        /*
         * Called when the Activity is no longer visible at all.
         * Stop updates and disconnect.
         */
        @Override
        public void onStop() {
            mLocationClient.disconnect();
            super.onStop();
        }

        /*
         * Called when the Activity is restarted, even before it becomes visible.
         */
        @Override
        public void onStart() {
            super.onStart();
            mLocationClient.connect();

        }

        /**
         * Verify that Google Play services is available before making a request.
         *
         * @return true if Google Play services is available, otherwise false
         */
        private boolean servicesConnected() {

            // Check that Google Play services is available
            int resultCode =
                    GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

            // If Google Play services is available
            if (ConnectionResult.SUCCESS == resultCode) {
                // In debug mode, log the status
                Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

                // Continue
                return true;
            // Google Play services was not available for some reason
            } else {
            	Toast.makeText(EditProfileActivity.this, "Cannot use geolation!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        /**
         * Invoked by the "Get Address" button.
         * Get the address of the current location, using reverse geocoding. This only works if
         * a geocoding service is available.
         *
         * @param v The view object associated with this method, in this case a Button.
         */
        // For Eclipse with ADT, suppress warnings about Geocoder.isPresent()
        @SuppressLint("NewApi")
        public void getAddress(View v) {

            // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
                // No geocoder is present. Issue an error message
                Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
                return;
            }

            if (servicesConnected()) {

                // Get the current location
                Location currentLocation = mLocationClient.getLastLocation();

                // Start the background task
                (new EditProfileActivity.GetAddressTask(this)).execute(currentLocation);
            }
        }

        @Override
        public void onConnected(Bundle bundle) {
      
        }

        /*
         * Called by Location Services if the connection to the
         * location client drops because of an error.
         */
        @Override
        public void onDisconnected() {} 

        /*
         * Called by Location Services if the attempt to
         * Location Services fails.
         */
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
            if (connectionResult.hasResolution()) {
                try {

                    // Start an Activity that tries to resolve the error
                    connectionResult.startResolutionForResult(
                            this,
                            LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                    /*
                    * Thrown if Google Play services canceled the original
                    * PendingIntent
                    */

                } catch (IntentSender.SendIntentException e) {

                    // Log the error
                    e.printStackTrace();
                }
            } else {

                // If no resolution is available, display a dialog to the user with the error.
            	Toast.makeText(EditProfileActivity.this, "Cannot use geolation!", Toast.LENGTH_SHORT).show();
            //	showErrorDialog(connectionResult.getErrorCode());
            }
        }

        /**
         * Report location updates to the UI.
         *
         * @param location The updated location.
         */
        @Override
        public void onLocationChanged(Location location) { } 

        /**
         * An AsyncTask that calls getFromLocation() in the background.
         * The class uses the following generic types:
         * Location - A {@link android.location.Location} object containing the current location,
         *            passed as the input parameter to doInBackground()
         * Void     - indicates that progress units are not used by this subclass
         * String   - An address passed to onPostExecute()
         */
        protected class GetAddressTask extends AsyncTask<Location, Void, String> {

            // Store the context passed to the AsyncTask when the system instantiates it.
            Context localContext;

            // Constructor called by the system to instantiate the task
            public GetAddressTask(Context context) {

                // Required by the semantics of AsyncTask
                super();

                // Set a Context for the background task
                localContext = context;
            }

            /**
             * Get a geocoding service instance, pass latitude and longitude to it, format the returned
             * address, and return the address to the UI thread.
             */
            @Override
            protected String doInBackground(Location... params) {
                /*
                 * Get a new geocoding service instance, set for localized addresses. This example uses
                 * android.location.Geocoder, but other geocoders that conform to address standards
                 * can also be used.
                 */
                Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

                // Get the current location from the input parameter list
                Location location = params[0];

                // Create a list to contain the result address
                List <Address> addresses = null;

                // Try to get an address for the current location. Catch IO or network problems.
                try {

                    /*
                     * Call the synchronous getFromLocation() method with the latitude and
                     * longitude of the current location. Return at most 1 address.
                     */
                    addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1
                    );

                    // Catch network or other I/O problems.
                    } catch (IOException exception1) {

                        // Log an error and return an error message
                        Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

                        // print the stack trace
                        exception1.printStackTrace();

                        // Return an error message
                        return (getString(R.string.IO_Exception_getFromLocation));

                    // Catch incorrect latitude or longitude values
                    } catch (IllegalArgumentException exception2) {

                        // Construct a message containing the invalid arguments
                        String errorString = getString(
                                R.string.illegal_argument_exception,
                                location.getLatitude(),
                                location.getLongitude()
                        );
                        // Log the error and print the stack trace
                        Log.e(LocationUtils.APPTAG, errorString);
                        exception2.printStackTrace();

                        //
                        return errorString;
                    }
                    // If the reverse geocode returned an address
                    if (addresses != null && addresses.size() > 0) {

                        // Get the first address
                        Address address = addresses.get(0);

                        // Format the first line of address
                        String addressText = getString(R.string.address_output_string,

                                // If there's a street address, add it
                                address.getMaxAddressLineIndex() > 0 ?
                                        address.getAddressLine(0) : "",

                                // Locality is usually a city
                                address.getLocality(),

                                // The country of the address
                                address.getCountryName()
                        );

                        // Return the text
                        return addressText;

                    // If there aren't any addresses, post a message
                    } else {
                      return getString(R.string.no_address_found);
                    }
            }

            /**
             * A method that's called once doInBackground() completes. Set the text of the
             * UI element that displays the address. This method runs on the UI thread.
             */
            @Override
            protected void onPostExecute(String address) {
                // Set the address in the UI
                myaddress.setText(address);
            }
        }
} 
