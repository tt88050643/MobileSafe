package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * ������ҳ�ĸ��࣬����Ҫ���嵥�ļ���ע�ᣬ��Ϊ����Ҫ����չʾ
 * 
 * @author zhaimeng
 * 
 */
public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mDetector;
	public SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		mDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					/**
					 * ���������¼�
					 * e1Ϊ��������ʼ��,e2Ϊ�����Ľ�����
					 * velocityΪ�ٶ�
					 */
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						//�ܾ����Ʋ��������
						if(Math.abs(e2.getRawY() - e1.getRawY())> 100){
							return true;
						}
						if(Math.abs(velocityX) < 100){
							return true;
						}
						
						// ���һ���,��һҳ
						if ((e2.getRawX() - e1.getRawX()) > 200) {
							showPreviousPage();
							return true;
						}
						// ���󻬶�,��һҳ
						if ((e1.getRawX() - e2.getRawX()) > 200) {
							showNextPage();
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	// ����ȥʵ���Լ��ġ���һҳ��������һҳ��
	public abstract void showNextPage();

	public abstract void showPreviousPage();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);// ��touch�¼�����mDetector����
		return super.onTouchEvent(event);
	}

	public void next(View view) {
		showNextPage();
	}

	public void previous(View view) {
		showPreviousPage();
	}
}
