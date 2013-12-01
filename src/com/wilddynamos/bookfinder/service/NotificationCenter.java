package com.wilddynamos.bookfinder.service;

import com.wilddynamos.bookfinder.ws.remote.action.WaitingForMessage;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * Service that starts the thread to listen to
 * 
 * @author JunqiWang
 * 
 */
public class NotificationCenter extends Service {

	/**
	 * The handler listens log out action so then stop connection with server
	 */
	public static final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message m) {
			if (m.what == -1)
				NotificationCenter.t.stopThread();
		}
	};

	/**
	 * The reading thread waiting for new requests and responds
	 */
	public static WaitingForMessage t;

	/**
	 * Establish a long connection with server at starting
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (t == null || !t.isAlive()) {
			t = new WaitingForMessage(this);
			t.start();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
