package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手机防盗
 * 
 * @author zhaimeng
 * 
 */
public class LostFindActivity extends Activity {
	private TextView tvSafePhone;
	private ImageView ivProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences mPrefs = getSharedPreferences("config", MODE_PRIVATE);

		boolean configed = mPrefs.getBoolean("configed", false);// 判断是否进入过了设置向导
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			// 设置安全号码
			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			tvSafePhone.setText(mPrefs.getString("safe_phone", ""));
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = mPrefs.getBoolean("protect", false);
			// 设置锁的图片
			if (protect) {
				ivProtect.setImageResource(R.drawable.lock);
			} else {
				ivProtect.setImageResource(R.drawable.unlock);
			}
		} else {
			// 跳转设置向导页
			startActivity(new Intent(LostFindActivity.this,
					Setup1Activity.class));
			finish();
		}
	}

	/**
	 * 重新进入设置向导
	 * 
	 * @param view
	 */
	public void reEnter(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
