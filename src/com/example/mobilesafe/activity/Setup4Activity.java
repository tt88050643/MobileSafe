package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 第四个设置向导页
 * 
 * @author zhaimeng
 * 
 */
public class Setup4Activity extends BaseSetupActivity {
	CheckBox cbProtect;
	private boolean protect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		protect = mPref.getBoolean("protect", false);
		if(protect){
			cbProtect.setText("防盗保护已经开启");
			cbProtect.setChecked(true);
		}else {
			cbProtect.setText("防盗保护没有开启");
			cbProtect.setChecked(false);
		}
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cbProtect.setText("防盗保护已经开启");
					mPref.edit().putBoolean("protect", true).commit();
				}else {
					cbProtect.setText("防盗保护没有开启");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	public void showNextPage() {
		startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("configed", true).commit();// 更新sp，表示已经展示过向导了
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void showPreviousPage() {
		startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}
	
}
