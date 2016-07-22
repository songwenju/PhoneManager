package com.wjustudio.handlerdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvMain = (TextView) findViewById(R.id.tv_main);
        mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvMain.setText(String.valueOf((i++)));
                mHandler.postDelayed(this,2000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
