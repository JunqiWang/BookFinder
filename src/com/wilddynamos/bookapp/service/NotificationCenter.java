package com.wilddynamos.bookapp.service;

import com.wilddynamos.bookapp.ws.remote.action.WaitingForMessage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationCenter extends Service {
	
	public static Thread t;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(t == null || !t.isAlive()) {
			t = new WaitingForMessage(this);
			t.start();
		}
		 
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
