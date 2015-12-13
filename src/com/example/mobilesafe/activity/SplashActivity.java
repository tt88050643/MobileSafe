package com.example.mobilesafe.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DAILOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;
	private TextView tvVersion;
	private TextView tvProgress;
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
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON����", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			}
		};
	};
	private SharedPreferences mPref;
	private boolean autoUpdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvProgress = (TextView) findViewById(R.id.tv_progress);
		tvVersion.setText("�汾�ţ�" + getVersionName());
		RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		//�ж��Ƿ���Ҫ�Զ�����
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		autoUpdate = mPref.getBoolean("auto_update", true);
		if(autoUpdate){
			checkVersion();
		}else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		//���䶯��Ч��
		AlphaAnimation anim = new AlphaAnimation((float) 0.3, 1);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);
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
		final long startTime = System.currentTimeMillis();
		new Thread(){
			@Override
			public void run() {
				Message message = Message.obtain();
				HttpURLConnection connection = null;
				try {
					URL url = new URL("http://192.168.101.123:8080/update.json");
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
						}else {
							message.what = CODE_ENTER_HOME;
						}
						Log.i("cool", mDesc);
					}
				}catch (MalformedURLException e) {
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
					long endTime = System.currentTimeMillis();
					long useTime = endTime - startTime;
					if(useTime < 2000){
						try {
							Thread.sleep(2000 - useTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
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
				download();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		//���ð�back�����ļ���
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		}); 
		builder.show();
	}
	
	/**
	 * ������ҳ��
	 */
	protected void enterHome(){
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	protected void download(){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String target = Environment.getExternalStorageDirectory() + "/update.apk";
			tvProgress.setVisibility(View.VISIBLE);//��ʾ����textview
			
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
				
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					tvProgress.setText("���ؽ��ȣ�" + (current*100 / total) + "%");
					Log.i("cool", current + " " + total);
				}
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					//���ظ��³ɹ�����װapp
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
					Log.i("cool", "�������");
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
				}
			});
		}else {
			Toast.makeText(SplashActivity.this, "û��SD��", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//�ڰ�װ����ʱ���û������ȡ�����������������
		if(requestCode == 0){
			enterHome();
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
