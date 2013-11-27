package com.wilddynamos.bookapp.ws.remote.action.profile;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.profile.EditProfileActivity;
import com.wilddynamos.bookapp.dblayout.UserDataSource;
import com.wilddynamos.bookapp.model.User;
import com.wilddynamos.bookapp.utils.DataUtils;
import com.wilddynamos.bookapp.ws.remote.Connection;

//public class EditMyProfile extends Thread {
//	
//	private EditProfileActivity a;
//	private Context context;
//	private User user;
//	private byte[] byteArray;
//	
//	public EditMyProfile(EditProfileActivity a, Context context, User user, byte[] byteArray) {
//		this.a = a;
//		this.context = context;
//		this.user = user;
//		this.byteArray = byteArray;
//	}
//	
//	public void run() {
//		try {
//			UserDataSource userDataSource = new UserDataSource(context);
//			userDataSource.open();
//			int sqliteResult = userDataSource.updateUser(user);
//			userDataSource.close();
//			//System.out.println(sqliteResult);
//			System.out.println(byteArray.length);
//			String imageString = new String(byteArray, Charset.forName("ISO-8859-1"));
//			System.out.println(imageString.length());
//			
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("id", String.valueOf(user.getId()));
//			params.put("name", user.getName());
//			params.put("gender", user.getGender() ? "M" : "F");
//			params.put("campus", user.getCampus());
//			params.put("contact", user.getContact());
//			params.put("address", user.getAddress());
//			params.put("image",imageString);
//			
//			InputStream is = Connection.requestByPost("/EditProfile", params);
//			BufferedReader br = new BufferedReader(new InputStreamReader(is));
//			String result = br.readLine();
//			
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//			a.getHandler().sendEmptyMessage(-1);
//		}
//	}
//
//}
public class EditMyProfile extends AsyncTask<String, Void, String> {

	private EditProfileActivity a;
	private Context context;
	private int sqliteResult;
	
	public EditMyProfile(EditProfileActivity a, Context context) {
		this.a = a;
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		//sqlite3 operations
		UserDataSource userDataSource = new UserDataSource(context);
		userDataSource.open();
		
		User user = new User();
		user.setId(Integer.parseInt(params[0]));
		user.setName(params[1]);
		user.setGender(params[2].equals("Male"));
		user.setCampus(params[3]);
		user.setContact(params[4]);
		user.setAddress(params[5]);
		user.setPhotoAddr(params[6]);
		
		sqliteResult = userDataSource.updateUser(user);
		userDataSource.close();
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("id", params[0]);
		paramsMap.put("name", params[1]);
		paramsMap.put("gender", params[2]);
		paramsMap.put("campus", params[3]);
		paramsMap.put("contact", params[4]);
		paramsMap.put("address", params[5]);
		paramsMap.put("image", params[7]);
		
		return DataUtils.receiveFlag(Connection.requestByPost("/PostEditBook", paramsMap));
	}

	@Override
	protected void onPostExecute(String result) {
		if ((sqliteResult != 1) && !result.equals("1"))
			Toast.makeText(a, "Both sqlite and mysql wrong!", Toast.LENGTH_SHORT).show();
		else if ((sqliteResult == 1) && !result.equals("1"))
			Toast.makeText(a, "Mysql wrong!", Toast.LENGTH_SHORT).show();
		else if ((sqliteResult != 1) && result.equals("1"))
			Toast.makeText(a, "Sqlite wrong!", Toast.LENGTH_SHORT).show();
		else if ((sqliteResult == 1) && result.equals("1")) {
			Toast.makeText(a, "Profile updated!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(a, MultiWindowActivity.class);
            intent.putExtra(MultiWindowActivity.TAB_SELECT, 2);
            a.startActivity(intent);
		}
		else 
			Toast.makeText(a, "What happened?", Toast.LENGTH_SHORT).show();
			
	}
}
