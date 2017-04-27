package com.chen.develop.zhihudaily.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.appoffers.OffersManager;
import com.baidu.mobads.appoffers.OffersView;
import com.baidu.mobads.appoffers.PointsUpdateListener;
import com.chen.develop.zhihudaily.R;

public class ShowInActivity extends Activity{

	boolean isShow=false;
	private RelativeLayout rlMain;
	private TextView point;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("test", "ShowInActivity.onCreate");
		setContentView(R.layout.show_in);
		rlMain= (RelativeLayout) findViewById(R.id.rl_rlMain);
		point= (TextView) findViewById(R.id.points);
//		RelativeLayout rlMain=new RelativeLayout(this);
// 打开积分墙，如果不使用广告位id，请调用此接口
		OffersManager.showOffers(ShowInActivity.this);

		// 如果使用以“用户维度回调”的“积分回调”方式，必须在广告展现前调用该接口，否则无法展现广告
//		OffersManager.setUserName(this, "user name");
		// 设置积分监听接口 subPoints、addPoints、getPoints方法调用在PointsUpdateListener中返回结果
		OffersManager.setPointsUpdateListener(this, new PointsUpdateListener() {

			@Override
			public void onPointsUpdateFailed(final String error) {
				Log.d("", "onPointsUpdateFailed:" + error);
				ShowInActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						point.setText(error);
					}
				});
			}

			@Override
			public void onPointsUpdateSuccess(final int totalPoints) {
				Log.d("", "onPointsUpdate:" + totalPoints);
				ShowInActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						point.setText("积分："+totalPoints);
					}
				});
			}
		});


		OffersView ov=new OffersView(ShowInActivity.this, true);
		RelativeLayout.LayoutParams rllp=new RelativeLayout.LayoutParams(-1,-1);
		rllp.addRule(RelativeLayout.BELOW, 100);
		rlMain.addView(ov, rllp);


	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("test", "ShowInActivity.onDestroy");
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("test", "ShowInActivity.onPause");
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("test", "ShowInActivity.onRestart");
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("test", "ShowInActivity.onResume");
	}
	
}
