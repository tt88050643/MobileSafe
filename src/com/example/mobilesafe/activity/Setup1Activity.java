package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * ��һ��������ҳ
 * 
 * @author zhaimeng
 * 
 */
public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	public void showNextPage() {
		startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
		finish();
		/**
		 * ����activity��ת���л��Ķ���
		 */
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void showPreviousPage() {}
}
