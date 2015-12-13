package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 第一个设置向导页
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
		 * 两个activity跳转的切换的动画
		 */
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void showPreviousPage() {}
}
