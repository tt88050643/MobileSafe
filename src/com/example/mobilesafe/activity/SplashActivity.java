package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private TextView tvVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号：" + getVersionName());
	}
	
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			Log.i("cool", versionName + " " + versionCode);
			return versionName;
		} catch (NameNotFoundException e) {
			//没有找到包名的时候
			e.printStackTrace();
		}
		return "";
		
	}

}
