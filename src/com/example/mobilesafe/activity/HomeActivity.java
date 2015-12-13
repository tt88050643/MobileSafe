package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.MD5Utils;

/**
 * 主页面
 * 
 * @author zhaimeng
 * 
 */
public class HomeActivity extends Activity {
	private GridView gvHome;
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		gvHome.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 8:
					// 设置中心
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					break;
				case 0:
					// 手机防盗
					showPasswordDialog();
				default:
					break;
				}
			}
		});
	}

	/**
	 * 显示密码弹窗
	 * 
	 * @author zhaimeng
	 * 
	 */
	protected void showPasswordDialog() {
		// 判断是否已经设置了密码
		String savedPassword = mPref.getString("password", null);
		if(!TextUtils.isEmpty(savedPassword)){
			showPasswordInputDialog();
		}else {
			showPasswordSetDialog();
		}
		
	}
	/**
	 * 输入密码弹窗
	 */
	protected void showPasswordInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_input_password, null);// 将自定义的布局文件设置给Dialog
		dialog.setView(view, 0, 0, 0, 0);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();//隐藏dialog
			}
		});

		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				if(!TextUtils.isEmpty(password)){
					String savedPassword = mPref.getString("password", null);
					Log.i("cool", savedPassword);
					if(MD5Utils.encode(password).equals(savedPassword)){
						Toast.makeText(HomeActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else {
						Toast.makeText(HomeActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空啊！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
	}

	/**
	 * 设置密码弹窗
	 */
	protected void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_set_password, null);// 将自定义的布局文件设置给Dialog
		dialog.setView(view, 0, 0, 0, 0);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();//隐藏dialog
			}
		});

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();
				if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)){
					if(password.equals(passwordConfirm)){
						mPref.edit().putString("password", MD5Utils.encode(password)).commit();
						Toast.makeText(HomeActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else {
						Toast.makeText(HomeActivity.this, "两次密码不一致啊！", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(HomeActivity.this, "输入框不能为空啊！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
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
