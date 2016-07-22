package com.wjustudio.retrofitdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wjustudio.retrofitdemo.duoshuo.DuoShuoActivity;
import com.wjustudio.retrofitdemo.phone.PhoneApi;
import com.wjustudio.retrofitdemo.phone.PhoneResult;
import com.wjustudio.retrofitdemo.phone.PhoneService;
import com.wjustudio.retrofitdemo.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtPhone;
    private Button mBtnQuery;
    private Button mBtnQureyRxjava;
    private Button mDuoShuo;
    private TextView mResultView;
    private PhoneApi mApi;
    private PhoneService mPhoneService;
    private String mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mBtnQuery = (Button) findViewById(R.id.query_view);
        mBtnQureyRxjava = (Button) findViewById(R.id.query_rxjava_view);
        mDuoShuo = (Button) findViewById(R.id.duo_shuo);
        mResultView = (TextView) findViewById(R.id.result_view);

        //初始化APi
        mApi = PhoneApi.getApi();
        mPhoneService = mApi.getPhoneService();

        mBtnQuery.setOnClickListener(this);
        mBtnQureyRxjava.setOnClickListener(this);
        mDuoShuo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.query_view:
                query();
                break;
            case R.id.query_rxjava_view:
                queryByRxJava();
                break;
            case R.id.duo_shuo:
                startActivity(new Intent(MainActivity.this,DuoShuoActivity.class));

                break;
        }
    }

    /**
     * RxJava做异步查询
     */
    private void queryByRxJava() {
        if (setDefaultText()) return;
        mPhoneService.getPhoneResult(PhoneApi.API_KEY,mNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PhoneResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PhoneResult result) {
                        if (result != null && result.getErrNum() == 0) {
                            PhoneResult.RetDataBean entity = result.getRetData();
                            mResultView.append("地址：" + entity.getCity());
                        }
                    }
                });
    }

    private void query() {
        if (setDefaultText()) return;


        Call<PhoneResult> call = mPhoneService.getResult(PhoneApi.API_KEY, mNumber);
        //3.发送请求

        call.enqueue(new Callback<PhoneResult>() {
            @Override
            public void onResponse(Call<PhoneResult> call, Response<PhoneResult> response) {
                //4.处理结果
                if (response.isSuccessful()) {
                    PhoneResult result = response.body();
                    if (result != null) {
                        PhoneResult.RetDataBean bean = result.getRetData();
                        LogUtil.i(this, "MainActivity.onResponse.bean:" + bean);
                        mResultView.setText("地址："+bean.getCity());
                    }
                }

            }

            @Override
            public void onFailure(Call<PhoneResult> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private boolean setDefaultText() {
        mResultView.setText("");
        mNumber = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(mNumber)){
            Toast.makeText(MainActivity.this,"号码不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
