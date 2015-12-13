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
 * 设置向导页的父类，不需要再清单文件中注册，因为不需要界面展示
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
					 * 监听滑动事件
					 * e1为滑动的起始点,e2为滑动的结束点
					 * velocity为速度
					 */
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						//拒绝手势操作的情况
						if(Math.abs(e2.getRawY() - e1.getRawY())> 100){
							return true;
						}
						if(Math.abs(velocityX) < 100){
							return true;
						}
						
						// 向右滑动,上一页
						if ((e2.getRawX() - e1.getRawX()) > 200) {
							showPreviousPage();
							return true;
						}
						// 向左滑动,下一页
						if ((e1.getRawX() - e2.getRawX()) > 200) {
							showNextPage();
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	// 子类去实现自己的“上一页”，“下一页”
	public abstract void showNextPage();

	public abstract void showPreviousPage();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);// 将touch事件交给mDetector处理
		return super.onTouchEvent(event);
	}

	public void next(View view) {
		showNextPage();
	}

	public void previous(View view) {
		showPreviousPage();
	}
}
