package com.scxh.chatapp;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainWeixin extends Activity {
	
	public static MainWeixin instance = null;
	 
	private ViewPager mTabPager;	
	private ImageView mTabHome,mTabChat,mTabSetting;
	private int currIndex = 0;// 当前页卡编号
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_weixin);
         //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        mTabHome = (ImageView) findViewById(R.id.img_weixin);
        mTabChat = (ImageView) findViewById(R.id.img_address);
        mTabSetting = (ImageView) findViewById(R.id.img_settings);
        
        mTabHome.setOnClickListener(new MyOnClickListener(0));
        mTabChat.setOnClickListener(new MyOnClickListener(1));
        mTabSetting.setOnClickListener(new MyOnClickListener(2));
        
      
      //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View home = mLi.inflate(R.layout.main_tab_home, null);
        View chat = mLi.inflate(R.layout.main_tab_chat, null);
        View seting = mLi.inflate(R.layout.main_tab_settings, null);
        
      //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(home);
        views.add(chat);
        views.add(seting);
      //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mTabPager.setAdapter(mPagerAdapter);
    }
    /**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};
    
	 /* 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mTabHome.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					mTabChat.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					mTabSetting.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				mTabChat.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					mTabHome.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 2) {
					mTabSetting.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				mTabSetting.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					mTabHome.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					mTabChat.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				break;
			}
			currIndex = arg0;
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
    		
        	if(menu_display){         //如果 Menu已经打开 ，先关闭Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		Intent intent = new Intent();
            	intent.setClass(MainWeixin.this,Exit.class);
            	startActivity(intent);
        	}
    	}
    	
    	return false;
    }

	public void startchat(View v) {      //小新  对话界面
		Intent intent = new Intent (MainWeixin.this,ChatActivity.class);			
		startActivity(intent);	
      }  
	public void exit_settings(View v) {                           //退出  伪“对话框”，其实是一个activity
		Intent intent = new Intent (MainWeixin.this,ExitFromSettings.class);			
		startActivity(intent);	
	 }
	
}
    
    

