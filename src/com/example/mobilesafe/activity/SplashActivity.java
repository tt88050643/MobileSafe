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
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON错误", Toast.LENGTH_SHORT).show();
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
		tvVersion.setText("版本号：" + getVersionName());
		RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		//判断是否需要自动更新
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		autoUpdate = mPref.getBoolean("auto_update", true);
		if(autoUpdate){
			checkVersion();
		}else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		//渐变动画效果
		AlphaAnimation anim = new AlphaAnimation((float) 0.3, 1);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);
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
						}else {
							message.what = CODE_ENTER_HOME;
						}
						Log.i("cool", mDesc);
					}
				}catch (MalformedURLException e) {
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
	 * 弹出升级对话框
	 */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
		builder.setTitle("最新版本：" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				download();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		//设置按back按键的监听
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		}); 
		builder.show();
	}
	
	/**
	 * 进入主页面
	 */
	protected void enterHome(){
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	protected void download(){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String target = Environment.getExternalStorageDirectory() + "/update.apk";
			tvProgress.setVisibility(View.VISIBLE);//显示进度textview
			
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
				
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					tvProgress.setText("下载进度：" + (current*100 / total) + "%");
					Log.i("cool", current + " " + total);
				}
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					//下载更新成功，安装app
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
					Log.i("cool", "下载完成");
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
				}
			});
		}else {
			Toast.makeText(SplashActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//在安装更新时，用户点击了取消按键，会进入以下
		if(requestCode == 0){
			enterHome();
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
