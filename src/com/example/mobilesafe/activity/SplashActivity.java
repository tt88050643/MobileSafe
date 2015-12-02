package com.example.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;
import com.example.mobilesafe.utils.StreamUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DAILOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	private TextView tvVersion;
	private String mVersionName;//服务器的版本名
	private int mVersionCode;//服务器的版本号
	private String mDesc;
	private String mDownloadUrl;//下载地址
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DAILOG:
				showUpdateDailog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT).show();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON错误", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号：" + getVersionName());
		checkVersion();
	}
	/**
	 * 返回本地App的版本名
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			//没有找到包名的时候
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 返回本地App的版本号
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			//没有找到包名的时候
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 从服务器获取版本信息进行校验
	 */
	private void checkVersion(){
		new Thread(){
			@Override
			public void run() {
				Message message = Message.obtain();
				HttpURLConnection connection = null;
				try {
					URL url = new URL("http://192.168.1.116:8080/update.json");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);//连接超时
					connection.setReadTimeout(5000);//读取超时
					connection.connect();
					if (connection.getResponseCode() == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						//解析JSON
						JSONObject jsonObject = new JSONObject(result);
						mVersionName = jsonObject.getString("versionName");
						mVersionCode = jsonObject.getInt("versionCode");
						mDesc = jsonObject.getString("description");
						mDownloadUrl = jsonObject.getString("downloadUrl");
						
						if(mVersionCode > getVersionCode()){
							//有更新，弹出升级对话框
							message.what = CODE_UPDATE_DAILOG;
						}
						Log.i("cool", mDesc);
					}
				} catch (MalformedURLException e) {
					//URL错误
					e.printStackTrace();
					message.what = CODE_URL_ERROR;
				}catch (IOException e) {
					e.printStackTrace();
					message.what = CODE_NET_ERROR;
				} catch (JSONException e) {
					e.printStackTrace(); //JSON解析失败错误
					message.what = CODE_JSON_ERROR;
				} finally {
					mHandler.sendMessage(message);
					if(connection != null){
						connection.disconnect();
					}
				}
			}
		}.start();
		
	}
	
	/**
	 * 弹出升级对话框
	 */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
		builder.setTitle("最新版本：" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("cool", "立即更新");
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("cool", "以后再说");
			}
		});
		builder.show();
	}
}
