/**
 * Copyright 2014 viktor.zhou
 */
package com.warmtel.chatrobot;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class HttpConnectionUtil {
	private final static String TAG = "HttpConnectionUtil";

	public static enum HttpMethod {
		GET, POST
	}

	/**
	 * 鍥炶皟鎺ュ彛
	 * 
	 * @author viktor
	 * 
	 */
	public interface HttpConnectionCallback {
		/**
		 * Call back method will be execute after the http request return.
		 * 
		 * @param response
		 *            the response of http request. The value will be null if
		 *            any error occur.
		 */
		void execute(String response);
	}

	/**
	 * 寮傛杩炴帴
	 * 
	 * @param url
	 *            缃戝潃
	 * @param method
	 *            Http鏂规硶,POST璺烥ET
	 * @param callback
	 *            鍥炶皟鏂规硶,杩斿洖缁欓〉闈㈡垨鍏朵粬鐨勬暟鎹�
	 */
	public void asyncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		
		asyncConnect(url, null, method, callback);
	}

	/**
	 * 鍚屾鏂规硶
	 * 
	 * @param url
	 *            缃戝潃
	 * @param method
	 *            Http鏂规硶,POST璺烥ET
	 * @param callback
	 *            鍥炶皟鏂规硶,杩斿洖缁欓〉闈㈡垨鍏朵粬鐨勬暟鎹�
	 */
	public void syncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		syncConnect(url, null, method, callback);
	}

	/**
	 * 寮傛甯﹀弬鏁版柟娉�
	 * 
	 * @param url
	 *            缃戝潃
	 * @param params
	 *            POST鎴朑ET瑕佷紶閫掔殑鍙傛暟
	 * @param method
	 *            鏂规硶,POST鎴朑ET
	 * @param callback
	 *            鍥炶皟鏂规硶
	 */
	public void asyncConnect(final String url,
			final Map<String, String> params, final HttpMethod method,
			final HttpConnectionCallback callback) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... param) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return asyncConnects(url, params, method, callback);
			}

			@Override
			protected void onPostExecute(String result) {
				callback.execute(result);
				super.onPostExecute(result);
			}

		}.execute();

	}

	/**
	 * 鍚屾甯﹀弬鏁版柟娉�
	 * 
	 * @param url
	 *            缃戝潃
	 * @param params
	 *            POST鎴朑ET瑕佷紶閫掔殑鍙傛暟
	 * @param method
	 *            鏂规硶,POST鎴朑ET
	 * @param callback
	 *            鍥炶皟鏂规硶
	 */
	public void syncConnect(final String url, final Map<String, String> params,
			final HttpMethod method, final HttpConnectionCallback callback) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				syncConnects(url, params, method, callback);
			}
		});

	}

	/**
	 * 鍚屾甯﹀弬鏁版柟娉�
	 * 
	 * @param url
	 *            缃戝潃
	 * @param params
	 *            POST鎴朑ET瑕佷紶閫掔殑鍙傛暟
	 * @param method
	 *            鏂规硶,POST鎴朑ET
	 * @param callback
	 *            鍥炶皟鏂规硶
	 */
	public void syncConnects(final String url,
			final Map<String, String> params, final HttpMethod method,
			final HttpConnectionCallback callback) {
		String json = null;
		BufferedReader reader = null;
		InputStream is = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = getRequest(url, params, method);
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				is = response.getEntity().getContent();
				reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null; s = reader
						.readLine()) {
					sb.append(s);
				}

				json = sb.toString();
			}
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		callback.execute(json);
	}
	public final static  String UESRAGENT_PHONE = "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A405 Safari/8536.25"; 
	public String asyncConnects(final String url,
			final Map<String, String> params, final HttpMethod method,
			final HttpConnectionCallback callback) {
		String json = null;
		BufferedReader reader = null;
		InputStream is = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = getRequest(url, params, method);
			request.setHeader("User-Agent", UESRAGENT_PHONE);
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				is = response.getEntity().getContent();
				reader = new BufferedReader(new InputStreamReader(is));
				// 鍒涘缓StringBuilder瀵硅薄鐢ㄤ簬瀛樺偍鎵�鏈夋暟鎹�
				StringBuilder sb = new StringBuilder();
				// 瀹氫箟String绫诲瀷鐢ㄤ簬鍌ㄥ瓨鍗曡鏁版嵁
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				json = sb.toString();
			}
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	/**
	 * POST璺烥ET浼犻�掑弬鏁颁笉鍚�,POST鏄殣寮忎紶鍙侴ET鏄樉寮忎紶鍙�
	 * 
	 * @param url
	 *            缃戝潃
	 * @param params
	 *            鍙傛暟
	 * @param method
	 *            鏂规硶
	 * @return
	 */
	private HttpUriRequest getRequest(String url, Map<String, String> params,
			HttpMethod method) {
		if (method.equals(HttpMethod.POST)) {
			List<NameValuePair> listParams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (String name : params.keySet()) {
					listParams.add(new BasicNameValuePair(name, params
							.get(name)));
				}
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						listParams);
				HttpPost request = new HttpPost(url);
				request.setEntity(entity);
				return request;
			} catch (UnsupportedEncodingException e) {
				// Should not come here, ignore me.
				throw new java.lang.RuntimeException(e.getMessage(), e);
			}
		} else {

			if (url.indexOf("?") < 0) {
				url += "?";
			}
			if (params != null) {
				for (String name : params.keySet()) {
					try {
						url += "&" + name + "="
								+ URLEncoder.encode(params.get(name), "UTF-8");

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			HttpGet request = new HttpGet(url);
			return request;
		}
	}

	/**
	 * 鏂囦欢涓嬭浇
	 * 
	 * @param is
	 * @throws IOException
	 */
	private void downLoadFile(InputStream is) {
		File file = new File("/sdcard/music/android1.apk");
		FileOutputStream out = null;
		BufferedOutputStream outs = null;
		try {
			out = new FileOutputStream(file);
			outs = new BufferedOutputStream(out);
			byte[] buf = new byte[128];
			while (is.read(buf) != -1) {
				outs.write(buf);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outs != null) {
					outs.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
