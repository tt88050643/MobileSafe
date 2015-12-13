package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * ��������
 * @author zhaimeng
 *
 */
public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;//��������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting); 
		final SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if(autoUpdate){
			sivUpdate.setChecked(true);
		}else {
			sivUpdate.setChecked(false);
		}
		sivUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�жϵ�ǰ�Ĺ�ѡ״̬
				if(sivUpdate.isChecked()){
					sivUpdate.setChecked(false);
					mPref.edit().putBoolean("auto_update", false).commit();
				}else {
					sivUpdate.setChecked(true);
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
