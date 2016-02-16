package com.wjustudio.phoneManager.utils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 作者：songwenju on 2015/12/20 23:38
 * 邮箱：songwenju01@163.com
 */
public class OkHttpUtil {
    //单例设计模式,这里是饿汉式,好处是内存中只有一个对象,可以在不同的线程中使用,节省内存.就像一块
    //内存地址可以同时被多次使用一样,而不是为每次使用都拷贝一次
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    static {
        //设置超时时间
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * 该方法不会开启异步线程
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public  static void enqueue(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步访问网络,且不在意返回结果(实现空的callback)
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    /**
     * 获得服务器返回的String
     * @param url
     * @return
     * @throws IOException
     */
    public static String getStringFromServer(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()){
            String responseUrl = response.body().string();
            return  responseUrl;
        } else {
            CommonUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast("网络连接错误!");
                }
            });
            throw new IOException("Unexpected code " + response);
        }
    }

}
