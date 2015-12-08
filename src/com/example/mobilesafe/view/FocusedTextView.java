package com.example.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
/**
 * ��ȡ�����TextView
 * @author zhaimeng
 *
 */
public class FocusedTextView extends TextView {
	//��style��ʽ�Ļ���ִ������
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	//������ʱִ������
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	//�ô���new����ʱִ������
	public FocusedTextView(Context context) {
		super(context);
	}
	/**
	 * ��ʾ��û�л�ȡ����
	 * �����Ҫ������������Ҫִ�д˷������鿴�Ƿ��н���
	 */
	@Override
	public boolean isFocused() {
		// TODO �Զ����ɵķ������
		return true;
	}
	
}
