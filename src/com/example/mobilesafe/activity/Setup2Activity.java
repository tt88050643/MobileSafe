package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;
import com.example.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 第二个设置向导页
 * 
 * @author zhaimeng
 * 
 */
public class Setup2Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		final SettingItemView sivSim = (SettingItemView) findViewById(R.id.siv_sim);
		if (!TextUtils.isEmpty(mPref.getString("sim", null))) {
			sivSim.setChecked(true);
		} else {
			sivSim.setChecked(false);
		}

		sivSim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivSim.isChecked()) {
					sivSim.setChecked(false);
					mPref.edit().remove("sim").commit();
				} else {
					sivSim.setChecked(true);
					// 保存sim卡信息
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);// 获取sim卡序列号
					String simSerialNumber = tm.getSimSerialNumber();
					mPref.edit().putString("sim", simSerialNumber).commit();

				}
			}
		});
	}

	public void showNextPage() {
		// 如果SIM卡没有绑定，就不允许进入下一个页面
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			ToastUtils.showToast(this, "必须绑定sim卡");
			return;
		}
		startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
		finish();
		/**
		 * 两个activity跳转的切换的动画
		 */
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	public void showPreviousPage() {
		startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
		finish();
		/**
		 * 两个activity跳转的切换的动画
		 */
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}
}
