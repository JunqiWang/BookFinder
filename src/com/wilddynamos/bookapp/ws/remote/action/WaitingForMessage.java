package com.wilddynamos.bookapp.ws.remote.action;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.wilddynamos.bookapp.R;
import com.wilddynamos.bookapp.activity.mybooks.MyPostDetailActivity;
import com.wilddynamos.bookapp.activity.mybooks.MyRequestDetailActivity;
import com.wilddynamos.bookapp.ws.remote.Connection;

public class WaitingForMessage extends Thread {
	
	private Service service;
	
	public WaitingForMessage(Service service) {
		this.service = service;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		InputStream is = Connection.waiting();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		NotificationManager notiMngr = (NotificationManager) 
				service.getSystemService(Context.NOTIFICATION_SERVICE);
		
		try {
			String str = br.readLine();
			
			while((str = br.readLine()) != null) {

				if(str.contains("Req")) {
					int bookId = Integer.parseInt(str.substring(3));
					Notification noti = new Notification(R.drawable.app_cover,  
				                "New Request Coming", System.currentTimeMillis());
					
					noti.flags = Notification.FLAG_AUTO_CANCEL;
					
					Intent notiIntent = new Intent(service, MyPostDetailActivity.class);
					notiIntent.putExtra("id", bookId);
					notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
					
					PendingIntent pendingIntent = PendingIntent.getActivity(service, 0,  
							notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					noti.setLatestEventInfo(service, "Book Finder", "New Request Coming", 
				                pendingIntent);
					
					notiMngr.notify(0, noti);
				} else 	if(str.contains("Res")) {
					int bookId = Integer.parseInt(str.substring(3));
					Notification noti = new Notification(R.drawable.app_cover,  
				                "New Response Coming", System.currentTimeMillis());
					
					noti.flags = Notification.FLAG_AUTO_CANCEL;
					
					Intent notiIntent = new Intent(service, MyRequestDetailActivity.class);
					notiIntent.putExtra("id", bookId);
					notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
					
					PendingIntent pendingIntent = PendingIntent.getActivity(service, 0,  
							notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					noti.setLatestEventInfo(service, "Book Finder", "New Response Coming", 
				                pendingIntent);
					
					notiMngr.notify(0, noti);
				}
			}
		} catch (Exception e) {
			try {
				br.close();
			} catch (Exception e1) {
			}
		}
	}
}
