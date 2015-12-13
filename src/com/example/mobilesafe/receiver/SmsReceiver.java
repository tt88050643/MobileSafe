package com.example.mobilesafe.receiver;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.LocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * 拦截短信
 * @author zhaimeng
 *
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for(Object object : objects){
			SmsMessage message = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = message.getOriginatingAddress();//短信来源号码
			String messageBody = message.getMessageBody();//短信内容
			Log.i("cool", messageBody);
			
			if("#*alarm*#".equals(messageBody)){
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();
				abortBroadcast();//中断短信的传递，系统的不会收到短信了
			}else if("#*alarm*#".equals(messageBody)){
				//获取经纬度坐标
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String location = sp.getString("location", "");
				Log.i("cool", location);
				abortBroadcast();
			}else if ("#*lockscreen*#".equals(messageBody)) {
				
				abortBroadcast();
			}else if ("#*wipedata*#".equals(messageBody)) {
				
				abortBroadcast();
			}
		}
	}

}
