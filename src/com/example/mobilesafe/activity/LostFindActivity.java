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
 * �ֻ�����
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

		boolean configed = mPrefs.getBoolean("configed", false);// �ж��Ƿ�������������
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			// ���ð�ȫ����
			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			tvSafePhone.setText(mPrefs.getString("safe_phone", ""));
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = mPrefs.getBoolean("protect", false);
			// ��������ͼƬ
			if (protect) {
				ivProtect.setImageResource(R.drawable.lock);
			} else {
				ivProtect.setImageResource(R.drawable.unlock);
			}
		} else {
			// ��ת������ҳ
			startActivity(new Intent(LostFindActivity.this,
					Setup1Activity.class));
			finish();
		}
	}

	/**
	 * ���½���������
	 * 
	 * @param view
	 */
	public void reEnter(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
