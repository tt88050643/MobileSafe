package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilesafe.R;

/**
 * ��ҳ��
 * 
 * @author zhaimeng
 * 
 */
public class HomeActivity extends Activity {
	private GridView gvHome;
	private String[] mItems = new String[] { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���",
			"����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 8:
					//��������
					startActivity(new Intent(HomeActivity.this, SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			tvItem.setText(mItems[position]);
			ivItem.setImageResource(mPics[position]);

			return view;
		}

	}
}
