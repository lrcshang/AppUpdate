package com.example.appupdatet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.bobo.utils.DiffUtils;
import com.bobo.utils.PatchUtils;

public class MainActivity extends Activity {
	private String oldapk = "/data/app/com.example.baiducenter-1/base.apk";//老版本 /data/app/com.example.baiducenter-1/base.apk
	private String newapk = "mnt/sdcard/app2.apk";//新版本
	private String pathapk = "mnt/sdcard/path.apk";//补丁
	private String complexapk = "mnt/sdcard/newapk.apk";//合成的apk
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialog = new ProgressDialog(this);
		dialog.setMessage("请稍后...");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	static {
		System.loadLibrary("diff");
	}

	public void onclick(View view) {
		switch (view.getId()) {
		case R.id.add: //合成新版本
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					PatchUtils.patch(oldapk, complexapk, pathapk);
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainActivity.this, "新版本生成完成。", 0).show();
							dialog.dismiss();
						}
					});
				}
			}).start();

			break;
		case R.id.diff://拆分版本生成补丁
			 ApplicationInfo applicationInfo = this.getApplicationContext().getApplicationInfo();
		        String apkPath = applicationInfo.sourceDir;
		        Toast.makeText(this,apkPath ,1000).show();
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					DiffUtils.genDiff(oldapk, newapk, pathapk);
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainActivity.this, "补丁生成完成。", 0).show();
							dialog.dismiss();
						}
					});
				}
			}).start();

			break;
		default:
			break;
		}
	}
}
