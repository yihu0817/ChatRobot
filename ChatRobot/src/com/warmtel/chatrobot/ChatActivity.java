package com.warmtel.chatrobot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.warmtel.chatrobot.HttpConnectionUtil.HttpConnectionCallback;
import com.warmtel.chatrobot.HttpConnectionUtil.HttpMethod;

/**
 * 
 */
public class ChatActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private Button mBtnSend;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private HttpConnectionUtil mHttpConnectionUtil;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_xiaohei);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);

		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

		mHttpConnectionUtil = new HttpConnectionUtil();
		
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.trim().length() > 0) {
			//--------显示发送信息到界面----------------
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("龙涛");
			entity.setMsgType(false);
			entity.setText(contString);

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();

			mEditTextContent.setText("");

			mListView.setSelection(mListView.getCount() - 1);
            //-----------------------------------------
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("key", "bc8fc0d407cb5b5718a6ce1ad503e1bb");
			map.put("info", contString);
			map.put("userid", "42383");
			mHttpConnectionUtil.asyncConnect("http://www.tuling123.com/openapi/api", map,
					HttpMethod.GET, new HttpConnectionCallback() {

						@Override
						public void execute(String response) {
							try {
								Log.v("tag", "response >>> :" + response); // {"code":100000,"text":"n . 游戏；比赛adj . 勇敢的vi . 赌博"}
								JSONObject jsonObject = new JSONObject(response);
								String message = (String) jsonObject.get("text");

								//-------------显示接收的信息到界面-------------------
								ChatMsgEntity entity = new ChatMsgEntity();
								entity.setDate(getDate());
								entity.setMsgType(true);
								entity.setName("小新");
								entity.setText(message);
								
								mDataArrays.add(entity);
								mAdapter.notifyDataSetChanged();
								
								mListView.setSelection(mListView.getCount() - 1);
								
								//-----------------------------
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});

		}
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	public void head_xiaohei(View v) { // 标题栏 返回按钮
		Intent intent = new Intent(ChatActivity.this, InfoXiaohei.class);
		startActivity(intent);
	}
}