package com.wjustudio.evenbusdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.greenrobot.event.EventBus;

/**
 * 作者：songwenju on 2016/1/13 22:40
 * 邮箱：songwenju01@163.com
 * EventBus 在事件触发的地方发送消息,在需要消息的地方接收消息.
 */
public class SecondActivity extends Activity implements View.OnClickListener {

    private Button mSkipButton;
    private Button mFirstEventBtn;
    private Button mSecondEventBtn;
    private Button mThirdEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mSkipButton = (Button) findViewById(R.id.btn_skip);
        mFirstEventBtn = (Button) findViewById(R.id.first_event);
        mSecondEventBtn = (Button) findViewById(R.id.second_event);
        mThirdEventBtn = (Button) findViewById(R.id.third_event);
        mSkipButton.setOnClickListener(this);
        mFirstEventBtn.setOnClickListener(this);
        mSecondEventBtn.setOnClickListener(this);
        mThirdEventBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
                EventBus.getDefault().post(new FirstEvent("第一个EventBus"));
                break;

            case R.id.first_event:
                EventBus.getDefault().post(new FirstEvent("FirstEvent btn clicked"));
                break;
            case R.id.second_event:
                EventBus.getDefault().post(new SecondEvent("SecondEvent btn clicked"));
                break;
            case R.id.third_event:
                EventBus.getDefault().post(new ThirdEvent("Third btn clicked"));
                break;
        }
    }
}
