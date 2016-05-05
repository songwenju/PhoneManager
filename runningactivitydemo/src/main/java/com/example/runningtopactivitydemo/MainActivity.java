package com.example.runningtopactivitydemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Button mButtonGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mButtonGuide = (Button) findViewById(R.id.button);
        Log.i("swj", "isNoOption:" + RunningActivityUtil.isNoOption(mContext));
        Log.i("swj", "isNoSwitch:" + RunningActivityUtil.isNoSwitch(mContext));

        mButtonGuide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (RunningActivityUtil.isNoOption(mContext)) {
                    if (!RunningActivityUtil.isNoSwitch(mContext)) {
                        Intent intent = new Intent(
                                Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                    } else {
                        String taskPacknameForMinu = RunningActivityUtil.getTaskPacknameForMinu(mContext);
                        Log.i("swj", taskPacknameForMinu);
                    }
                }
            }
        });


    }
}
