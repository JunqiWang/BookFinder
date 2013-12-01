package com.wilddynamos.bookfinder.ws.remote.action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.wilddynamos.bookfinder.R;
import com.wilddynamos.bookfinder.activity.mybooks.MyPostDetailActivity;
import com.wilddynamos.bookfinder.activity.mybooks.MyRequestDetailActivity;
import com.wilddynamos.bookfinder.ws.remote.Connection;

/**
 * The thread that listens reads incoming requests or responds from server
 * 
 * @author JunqiWang
 * 
 */
public class WaitingForMessage extends Thread {

	/**
	 * The service that starts this thread
	 */
	private Service service;

	/**
	 * If continue reading
	 */
	private boolean canRun;

	private BufferedReader br = null;

	public WaitingForMessage(Service service) {
		this.service = service;
		this.canRun = true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		InputStream is = Connection.waiting();

		br = new BufferedReader(new InputStreamReader(is));

		NotificationManager notiMngr = (NotificationManager) service
				.getSystemService(Context.NOTIFICATION_SERVICE);

		try {
			String str;

			while (canRun && (str = br.readLine()) != null) {

				int bookId = Integer.parseInt(str.substring(3));

				if (str.contains("Req")) {
					Notification noti = new Notification(R.drawable.app_cover,
							"New Request Coming", System.currentTimeMillis());

					noti.flags = Notification.FLAG_AUTO_CANCEL;

					Intent notiIntent = new Intent(service,
							MyPostDetailActivity.class);
					notiIntent.putExtra("id", bookId);
					notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);

					PendingIntent pendingIntent = PendingIntent.getActivity(
							service, 0, notiIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);

					noti.setLatestEventInfo(service, "Book Finder",
							"New Request Coming", pendingIntent);

					notiMngr.notify(11, noti);
				} else if (str.contains("Res")) {
					Notification noti = new Notification(R.drawable.app_cover,
							"New Response Coming", System.currentTimeMillis());

					noti.flags = Notification.FLAG_AUTO_CANCEL;

					Intent notiIntent = new Intent(service,
							MyRequestDetailActivity.class);
					notiIntent.putExtra("id", bookId);
					notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);

					PendingIntent pendingIntent = PendingIntent.getActivity(
							service, 0, notiIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);

					noti.setLatestEventInfo(service, "Book Finder",
							"New Response Coming", pendingIntent);

					notiMngr.notify(13, noti);
				}
			}
		} catch (Exception e) {
			try {
				br.close();
			} catch (Exception e1) {
			}
		}
	}

	/**
	 * This method is called when user logs out
	 */
	public void stopThread() {
		this.canRun = false;
		try {
			service.stopSelf();
		} catch (Exception e) {
		}
	}
}
