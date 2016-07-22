package com.example.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * songwenju on 16-7-18 : 16 : 35.
 * 邮箱：songwenju@outlook.com
 */
public class ButtonActivity extends Activity {
    private static final String TAG = "Rxjava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.just("1")
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        //第一个线程操作
                        Log.i(TAG, "flat1---" + s + ", current thread :" + Thread.currentThread().getName());
                        //current thread :RxCachedThreadScheduler-1
                        return Observable.just("3");
                    }
                }).subscribeOn(Schedulers.io()) //指定当前为IO线程,
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        //第二个线程操作
                        Log.i(TAG, "flat2---" + s + ", current thread :" + Thread.currentThread().getName());
                        return Observable.just(s);
                    }
                }).subscribeOn(Schedulers.newThread()) //指定当前为新线程
                .observeOn(AndroidSchedulers.mainThread())//回调在android线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("Rxjava", "end --" + s + ", current thread :" + Thread.currentThread().getName()); //回调结果
                    }
                });

    }
}
