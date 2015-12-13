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
					// ��������
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					break;
				case 0:
					// �ֻ�����
					showPasswordDialog();
				default:
					break;
				}
			}
		});
	}

	/**
	 * ��ʾ���뵯��
	 * 
	 * @author zhaimeng
	 * 
	 */
	protected void showPasswordDialog() {
		// �ж��Ƿ��Ѿ�����������
		String savedPassword = mPref.getString("password", null);
		if(!TextUtils.isEmpty(savedPassword)){
			showPasswordInputDialog();
		}else {
			showPasswordSetDialog();
		}
		
	}
	/**
	 * �������뵯��
	 */
	protected void showPasswordInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_input_password, null);// ���Զ���Ĳ����ļ����ø�Dialog
		dialog.setView(view, 0, 0, 0, 0);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();//����dialog
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
						Toast.makeText(HomeActivity.this, "��½�ɹ���", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else {
						Toast.makeText(HomeActivity.this, "�������", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(HomeActivity.this, "��������ݲ���Ϊ�հ���", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.show();
	}

	/**
	 * �������뵯��
	 */
	protected void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_set_password, null);// ���Զ���Ĳ����ļ����ø�Dialog
		dialog.setView(view, 0, 0, 0, 0);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();//����dialog
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
						Toast.makeText(HomeActivity.this, "���óɹ���", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else {
						Toast.makeText(HomeActivity.this, "�������벻һ�°���", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(HomeActivity.this, "�������Ϊ�հ���", Toast.LENGTH_SHORT).show();
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
