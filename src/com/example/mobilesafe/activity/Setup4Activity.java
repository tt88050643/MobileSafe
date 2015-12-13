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
 * ���ĸ�������ҳ
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
			cbProtect.setText("���������Ѿ�����");
			cbProtect.setChecked(true);
		}else {
			cbProtect.setText("��������û�п���");
			cbProtect.setChecked(false);
		}
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cbProtect.setText("���������Ѿ�����");
					mPref.edit().putBoolean("protect", true).commit();
				}else {
					cbProtect.setText("��������û�п���");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	public void showNextPage() {
		startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("configed", true).commit();// ����sp����ʾ�Ѿ�չʾ������
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void showPreviousPage() {
		startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}
	
}
