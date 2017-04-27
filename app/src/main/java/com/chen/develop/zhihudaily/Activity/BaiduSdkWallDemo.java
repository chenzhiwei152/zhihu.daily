package com.chen.develop.zhihudaily.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobads.appoffers.OffersManager;
import com.baidu.mobads.appoffers.PointsUpdateListener;
import com.chen.develop.zhihudaily.R;

public class BaiduSdkWallDemo extends Activity {
	private Button btnOpenWall;
	private Button btnAddPoint;
	private Button btnSubPoint;
	private Button btnShowInView;
	private TextView currentPointTextView;
	private String currencyName = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.baidu_ad);

		currentPointTextView = (TextView) findViewById(R.id.info);

		// 代码设置APPSID与计费名，下面的设置方法与配置文件中的设置是等价的。
		// OffersManager.setAppSid(appSid);
		
		// 如果使用以“用户维度回调”的“积分回调”方式，必须在广告展现前调用该接口，否则无法展现广告
		OffersManager.setUserName(this, "user name");
		// 设置积分监听接口 subPoints、addPoints、getPoints方法调用在PointsUpdateListener中返回结果
		OffersManager.setPointsUpdateListener(this, new PointsUpdateListener() {

			@Override
			public void onPointsUpdateFailed(final String error) {
				Log.d("", "onPointsUpdateFailed:" + error);
				BaiduSdkWallDemo.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						currentPointTextView.setText(error);
					}
				});
			}

			@Override
			public void onPointsUpdateSuccess(final int totalPoints) {
				Log.d("", "onPointsUpdate:" + totalPoints);
				BaiduSdkWallDemo.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						currentPointTextView.setText(totalPoints + currencyName);
					}
				});
			}
		});
		btnOpenWall = (Button) findViewById(R.id.action);
		btnOpenWall.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 打开积分墙，如果不使用广告位id，请调用此接口
				OffersManager.showOffers(BaiduSdkWallDemo.this);
			}
		});
		btnAddPoint = (Button) this.findViewById(R.id.addTen);
		btnSubPoint = (Button) this.findViewById(R.id.subTwnty);
		btnAddPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 增加10积分
				OffersManager.addPoints(BaiduSdkWallDemo.this, 10);
			}
		});
		btnSubPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 减去20积分
				OffersManager.subPoints(BaiduSdkWallDemo.this, 20);
			}
		});

		btnShowInView = (Button) this.findViewById(R.id.inner);
		btnShowInView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 内嵌式积分墙
				Intent intent = new Intent(BaiduSdkWallDemo.this, ShowInActivity.class);
				startActivity(intent);
			}
		});
		this.currencyName = OffersManager.getCurrencyName(BaiduSdkWallDemo.this);
		OffersManager.getPoints(this);
	}
}