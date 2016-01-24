package com.wjustudio.evenbusdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mSkipButton;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        //注册EventBus
        EventBus.getDefault().register(this);
        mSkipButton = (Button) findViewById(R.id.btn_skip);
        mSkipButton.setOnClickListener(this);
        mTv = (TextView) findViewById(R.id.tv_main);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    /**
     * 接收消息,谁发的就用哪个对象接收
     * @param event
     */
    public void onEventMainThread(FirstEvent event){
        String msg = "MainThread 接收到的消息是:"+event.getMsg();
        mTv.setText(msg);
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 接收消息
     * @param event
     */
    public void onEventMainThread(SecondEvent event){
        String msg = "MainThread 接收到的消息是:"+event.getMsg();
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    /**
     * 接收消息
     * @param event
     */
    public void onEvent(ThirdEvent event){
        String msg = "MainThread 接收到的消息是:"+event.getMsg();
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
