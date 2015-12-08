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
		sivUpdate.setTitle("�����Զ�����");
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if(autoUpdate){
			sivUpdate.setDesc("�Զ������ѿ���");
			sivUpdate.setChecked(true);
		}else {
			sivUpdate.setDesc("�Զ������ѹر�");
			sivUpdate.setChecked(false);
		}
		sivUpdate.setDesc("�����Զ������ѿ���");
		sivUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�жϵ�ǰ�Ĺ�ѡ״̬
				if(sivUpdate.isChecked()){
					sivUpdate.setChecked(false);
					sivUpdate.setDesc("�Զ������ѹر�");
					mPref.edit().putBoolean("auto_update", false).commit();
				}else {
					sivUpdate.setChecked(true);
					sivUpdate.setDesc("�Զ������ѿ���");
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
