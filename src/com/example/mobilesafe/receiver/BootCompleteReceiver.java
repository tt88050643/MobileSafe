package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 监听手机开机的broadcast receiver
 * 
 * @author zhaimeng
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		Boolean protect = sp.getBoolean("protect", false);
		if (protect) {
			String sim = sp.getString("sim", null);// 检查sp中保存的是否“绑定sim”
			if (!TextUtils.isEmpty(sim)) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber();
				if (sim.equals(currentSim)) {
					Log.i("cool", "手机安全");
				} else {
					Log.i("cool", "手机危险，发送报警短信!!!");
					String phone = sp.getString("safe_phone", "");
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null, "sim card changged!", null, null);
				}
			}
		} else {
			return;
		}

	}
}
