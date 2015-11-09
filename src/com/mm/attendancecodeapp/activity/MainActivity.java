package com.mm.attendancecodeapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miebo.utils.BaseActivity;
import com.miebo.utils.BaseUtil;
import com.mm.attendancecodeapp.adapter.RecordsAdapter;
import com.mm.attendancecodeapp.bean.records;

public class MainActivity extends BaseActivity implements OnClickListener {

	private List<records> list;
	private RecordsAdapter adapter;
	private ListView listview1;
	private Button btnSign;
	private final Gson gson = new Gson();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findview();
		new loadAsyncTask().execute();
		BaseUtil.LogII("onCreate");
	}

	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("考勤");
		btnSign = (Button) findViewById(R.id.btnSign);
		btnSign.setOnClickListener(this);

		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//showContextDialog(position);
			}

		});
	}

	private class loadAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainActivity.this, "提示", "获取中,请稍后..");
		}

		@Override
		protected String doInBackground(String... params) {
			String urlString = AppConstant.getUrl(getApplicationContext()) + "Action=getrecords";
			urlString += "&userid=" + user.getId();
			BaseUtil.LogII(urlString);
			return httpHelper.HttpRequest(urlString);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			BaseUtil.LogII("result " + result);
			dialog.dismiss();
			if (result != null && result.trim().length() > 0) {
				list = gson.fromJson(result, new TypeToken<List<records>>() {}.getType());

			} else {
				list = new ArrayList<records>();
				toastUtil.show("没有数据");
			}
			adapter = new RecordsAdapter(MainActivity.this, list);
			listview1.setAdapter(adapter);
		}
	}

	private class deleteAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainActivity.this, "提示", "处理中,请稍后..");
		}

		@Override
		protected String doInBackground(String... params) {
			String urlString = AppConstant.getUrl(getApplicationContext()) + "Action=Del";
			urlString += "&Table=records";
			urlString += "&ID=" + params[0];
			BaseUtil.LogII(urlString);
			return httpHelper.HttpRequest(urlString);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if ("Success".equals(result)) {
				toastUtil.show("删除成功");
				new loadAsyncTask().execute();
			} else {
				toastUtil.show("删除失败");
			}
		}
	}

	// 弹出上下文菜单
	private void showContextDialog(final int position) {
		String[] arg = new String[] { "删除签到" };
		new AlertDialog.Builder(this).setTitle("选择操作").setItems(arg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new deleteAsyncTask().execute(list.get(position).getId() + "");
			}
		}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1) {
			new loadAsyncTask().execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 100, 0, "我的信息").setIcon(R.drawable.icon_application);
		menu.add(0, 102, 0, "退出").setIcon(R.drawable.icon_application);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 100:
			intent = new Intent(MainActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;

		case 102:
			finish();
			System.exit(0);
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnSign:
			intent = new Intent(MainActivity.this, CaptureActivity.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}