package com.wjustudio.phoneManager.activities;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;

public class QrCodeScanActivity extends Activity {
	/**
	 * 显示扫描结果
	 */
	private TextView mTextView ;
	/**
	 * 显示扫描拍的图片
	 */
	private ImageView mImageView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_scan);

		mTextView = (TextView) findViewById(R.id.result);
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);

		//扫描完了之后调到该界面
		Bundle bundle = this.getIntent().getExtras();
		//显示扫描到的内容
		mTextView.setText(bundle.getString("result"));
		//显示
//		mImageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra("bitmap"));
	}

}
