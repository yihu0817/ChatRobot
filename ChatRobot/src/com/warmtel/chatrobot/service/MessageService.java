package com.warmtel.chatrobot.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.warmtel.chatrobot.ChatActivity;
import com.warmtel.chatrobot.R;

public class MessageService extends Service {
	
    public static void startMessageService(Context context){
    	Intent intentService = new Intent(context,MessageService.class);
    	context.startService(intentService);
    }
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("tag", "onCreate  >>> ");
		
		for(int i = 1; i <= 2; i++){
			final int number = i; 
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Log.v("tag", "sendMessageNotify  >>> ");
					sendMessageNotify(number);
				}
			}, 3000);
		}
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("tag", "onStartCommand  >>> ");
		return START_STICKY;
	    
	}
	
	@Override
    public void onDestroy() {
		super.onDestroy();
		
		Log.v("tag", "onDestroy  >>> ");
    }
	
	
	public void sendMessageNotify(int number){
		String text = "您有新消息";
		String title = "消息通知";
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.copyright, text, System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), title,text, PendingIntent.getActivity(MessageService.this, 0,
				new Intent(MessageService.this,
						ChatActivity.class), 0));
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.number = number;
		
		manager.notify(111, notification);
	}
}
