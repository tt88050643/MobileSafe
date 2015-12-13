package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 第三个设置向导页
 * 
 * @author zhaimeng
 * 
 */
public class Setup3Activity extends BaseSetupActivity {
	private EditText etPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etPhone.setText(mPref.getString("safe_phone", ""));
	}
 
	public void showNextPage() {
		String phone = etPhone.getText().toString();
		if(TextUtils.isEmpty(phone)){
			ToastUtils.showToast(this, "安全号码不能为空");
			return;
		}
		mPref.edit().putString("safe_phone", phone).commit();//保存安全号码
		startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
		finish();
		/**
		 * 两个activity跳转的切换的动画
		 */
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void showPreviousPage() {
		startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
		finish();
		/**
		 * 两个activity跳转的切换的动画
		 */
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
	}
	
	public void selectContact(View view){
		startActivityForResult(new Intent(this, ContactActivity.class), 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			String phone = data.getStringExtra("phone");
			phone.replaceAll("-", "");
			phone.replaceAll(" ", "");
			etPhone.setText(phone);
		}
		
	}
}
