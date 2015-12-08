package com.example.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
/**
 * 获取焦点的TextView
 * @author zhaimeng
 *
 */
public class FocusedTextView extends TextView {
	//有style样式的话会执行以下
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	//有属性时执行以下
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	//用代码new对象时执行以下
	public FocusedTextView(Context context) {
		super(context);
	}
	/**
	 * 表示有没有获取焦点
	 * 跑马灯要跑起来，首先要执行此方法，查看是否有焦点
	 */
	@Override
	public boolean isFocused() {
		// TODO 自动生成的方法存根
		return true;
	}
	
}
