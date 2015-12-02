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
	private String mVersionName;//�������İ汾��
	private int mVersionCode;//�������İ汾��
	private String mDesc;
	private String mDownloadUrl;//���ص�ַ
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DAILOG:
				showUpdateDailog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url����", Toast.LENGTH_SHORT).show();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT).show();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON����", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("�汾�ţ�" + getVersionName());
		checkVersion();
	}
	/**
	 * ���ر���App�İ汾��
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			//û���ҵ�������ʱ��
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * ���ر���App�İ汾��
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			//û���ҵ�������ʱ��
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * �ӷ�������ȡ�汾��Ϣ����У��
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
					connection.setConnectTimeout(5000);//���ӳ�ʱ
					connection.setReadTimeout(5000);//��ȡ��ʱ
					connection.connect();
					if (connection.getResponseCode() == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						//����JSON
						JSONObject jsonObject = new JSONObject(result);
						mVersionName = jsonObject.getString("versionName");
						mVersionCode = jsonObject.getInt("versionCode");
						mDesc = jsonObject.getString("description");
						mDownloadUrl = jsonObject.getString("downloadUrl");
						
						if(mVersionCode > getVersionCode()){
							//�и��£����������Ի���
							message.what = CODE_UPDATE_DAILOG;
						}
						Log.i("cool", mDesc);
					}
				} catch (MalformedURLException e) {
					//URL����
					e.printStackTrace();
					message.what = CODE_URL_ERROR;
				}catch (IOException e) {
					e.printStackTrace();
					message.what = CODE_NET_ERROR;
				} catch (JSONException e) {
					e.printStackTrace(); //JSON����ʧ�ܴ���
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
	 * ���������Ի���
	 */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
		builder.setTitle("���°汾��" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("cool", "��������");
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("cool", "�Ժ���˵");
			}
		});
		builder.show();
	}
}
