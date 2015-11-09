package com.mm.attendancecodeapp.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miebo.utils.BaseActivity;
import com.miebo.utils.BaseUtil;
import com.miebo.utils.HardwareHelper;
import com.mm.attendancecodeapp.bean.users;

/**
 * 
 * @author zlus
 * 
 */
public class RegisterActivity extends BaseActivity {

	private Button btnLogin, btnRegister;
	private EditText etLoginID, etPassword, etPasswordOK, etName, etEmail;
	private EditText etSpecialty, etClasss, etPhone;

	private SubmitAsyncTask submitloadAsyncTask;
	private int id = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findview();
		setListener();
		if (user != null) {
			id = user.getId();
			((TextView) findViewById(R.id.tvTopTitleCenter)).setText("�ҵ���Ϣ");
			etLoginID.setText(user.getLoginid());
			etName.setText(user.getName());
			btnRegister.setText("�޸�");
			btnLogin.setText("ȡ��");
			new loadAsyncTask().execute();

		}

	}

	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("ע��");
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		etLoginID = (EditText) findViewById(R.id.etLoginID);
		etPassword = (EditText) findViewById(R.id.etPassword);

		etPasswordOK = (EditText) findViewById(R.id.etPasswordOK);
		etName = (EditText) findViewById(R.id.etName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etSpecialty = (EditText) findViewById(R.id.etSpecialty);
		etClasss = (EditText) findViewById(R.id.etClasss);

	}

	private void setListener() {
		btnRegister.setOnClickListener(new btnRegisterOnClickListener());
		btnLogin.setOnClickListener(this);
	}

	@SuppressWarnings("unchecked")
	private class btnRegisterOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (etLoginID.getText().length() == 0) {
				toastUtil.show("������ѧ��");
				return;
			}

			if (etName.getText().length() == 0) {
				toastUtil.show("����������");
				return;
			}
			if (etPassword.getText().length() == 0) {
				toastUtil.show("����������");
				return;
			}

			if (etPasswordOK.getText().length() == 0) {
				toastUtil.show("���ٴ���������");
				return;
			}
			if (!etPassword.getText().toString().equals(etPasswordOK.getText().toString())) {
				toastUtil.show("������������벻һ��");
				return;
			}

			BaseUtil.HideKeyboard(RegisterActivity.this);
			submitloadAsyncTask = new SubmitAsyncTask();
			submitloadAsyncTask.execute("");

		}
	};

	@SuppressWarnings("deprecation")
	private class SubmitAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(RegisterActivity.this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("������,���Ժ�..");
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (submitloadAsyncTask != null) {
						submitloadAsyncTask.cancel(true);
						submitloadAsyncTask = null;
						toastUtil.show("������ȡ��");
					}
				}
			});
			dialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String urlString = AppConstant.getUrl(getApplicationContext()) + "Action=register";

			urlString += "&id=" + id;
			urlString += "&loginid=" + etLoginID.getText();
			urlString += "&passwords=" + etPassword.getText();
			urlString += "&name=" + etName.getText();
			urlString += "&phone=" + etPhone.getText();
			urlString += "&email=" + etEmail.getText();
			urlString += "&classs=" + etClasss.getText();
			urlString += "&specialty=" + etSpecialty.getText();
			urlString += "&imei=" + HardwareHelper.getIMEI(RegisterActivity.this);
			String result = httpHelper.HttpRequest(urlString);

			BaseUtil.LogII(urlString);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			submitloadAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("1")) {
				if (id == 0) {
					toastUtil.show("ע��ɹ�");
				} else {
					toastUtil.show("�޸ĳɹ�");

				}
				finish();
			} else {
				toastUtil.show("����ʧ��");
			}
		}
	}

	private class loadAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(RegisterActivity.this, "��ʾ", "��ȡ��..");
		}

		@Override
		protected String doInBackground(String... params) {
			String urlString = AppConstant.getUrl(getApplicationContext()) + "Action=getuser";
			urlString += "&userid=" + user.getId();
			String json = httpHelper.HttpRequest(urlString);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			List<users> list = new Gson().fromJson(result, new TypeToken<List<users>>() {}.getType());
			users model = list.get(0);

			etEmail.setText(model.getEmail());
			etLoginID.setText(model.getLoginid());
			etName.setText(model.getName());
			etPassword.setText(model.getPasswords());
			etPasswordOK.setText(model.getPasswords());
			etPhone.setText(model.getPhone());
			etSpecialty.setText(model.getSpecialty());

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			finish();
			break;

		}
	}

}
