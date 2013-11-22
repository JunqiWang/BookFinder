package com.wilddynamos.bookapp.ws.remote.action;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.MultiWindowActivity;
import com.wilddynamos.bookapp.activity.post.PostListActivity;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class WaitingRequest extends Thread {
	
	public static String s = "K";
	
	private Service service;
	
	public WaitingRequest(Service service) {
		this.service = service;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {System.out.println("Thread started");
		InputStream is = Connection.waiting();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		try {
			while((s = br.readLine()) != null) {

				if(s.contains("New")) {
					Notification notification = new Notification(R.drawable.app_cover,  
				                "New Request", System.currentTimeMillis());  
					
					Intent notificationIntent = new Intent(service, MultiWindowActivity.class);  
					
					PendingIntent pendingIntent = PendingIntent.getActivity(service, 0,  
				                notificationIntent, 0);  
					
					notification.setLatestEventInfo(service, "New Request", null,  
				                pendingIntent);
					
					service.startForeground(1, notification);  
				}
			}
		} catch (IOException e) {
			try {
				br.close();
			} catch (IOException e1) {
			}
		}
	}
}
