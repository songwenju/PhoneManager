package com.wjustudio.retrofitdemo.phone;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 手机号相关的API
 * songwenju on 16-7-22 : 17 : 37.
 * 邮箱：songwenju@outlook.com
 */
public class PhoneApi {
    /**
     * HOST地址
     */
    public static final String BASE_URL = "http://apis.baidu.com";
    /**
     * 开发者key
     */
    public static final String API_KEY = "19c7d440579f1e183cbc7d21e919aeff";

    /**
     * 获取PhoneApi实例
     * @return
     */
    public static PhoneApi getApi(){
        return ApiHolder.sPhoneApi;
    }

    static class ApiHolder{
        private static PhoneApi sPhoneApi = new PhoneApi();
    }

    private PhoneService mPhoneService;

    private PhoneApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mPhoneService = retrofit.create(PhoneService.class);
    }

    /**
     * 获取PhoneService实例
     * @return
     */
    public PhoneService getPhoneService(){
        return mPhoneService;
    }
}
