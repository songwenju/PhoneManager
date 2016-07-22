package com.wjustudio.retrofitdemo.phone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * songwenju on 16-7-22 : 15 : 11.
 * 邮箱：songwenju@outlook.com
 */
public interface PhoneService {

    /**
     * get请求
     * 1.括号内的参数是AIP的相对地址,它的基础地址在retrofit初始化时会设定，这里是http://apis.baidu.com
     * 2.header 这里表示apiId=xxx
     * 3.Query表示查询
     */
    @GET("/apistore/mobilenumber/mobilenumber")
    Call<PhoneResult> getResult(
            @Header("apikey") String apikey,
            @Query("phone") String phone);

    @GET("/apistore/mobilenumber/mobilenumber")
    Observable<PhoneResult> getPhoneResult(
            @Header("apikey") String apikey,
            @Query("phone") String phone);

}
