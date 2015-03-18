package com.warmtel.chatrobot;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.warmtel.chatrobot.service.MessageService;

public class LoadingActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.loading);
			
	new Handler().postDelayed(new Runnable(){
		@Override
		public void run(){
			Intent intent = new Intent (LoadingActivity.this,Whatsnew.class);			
			startActivity(intent);			
			LoadingActivity.this.finish();
			
			Log.v("tag", "Æô¶¯·þÎñ");
			MessageService.startMessageService(LoadingActivity.this);
			
		}
	}, 2000);
   }
}