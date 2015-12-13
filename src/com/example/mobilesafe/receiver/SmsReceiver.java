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
 * ���ض���
 * @author zhaimeng
 *
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for(Object object : objects){
			SmsMessage message = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = message.getOriginatingAddress();//������Դ����
			String messageBody = message.getMessageBody();//��������
			Log.i("cool", messageBody);
			
			if("#*alarm*#".equals(messageBody)){
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();
				abortBroadcast();//�ж϶��ŵĴ��ݣ�ϵͳ�Ĳ����յ�������
			}else if("#*alarm*#".equals(messageBody)){
				//��ȡ��γ������
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
