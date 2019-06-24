package com.example.ayomide.androideatit.Remote;

import com.example.ayomide.androideatit.Model.MyResponse;
import com.example.ayomide.androideatit.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//Retrofit Client to send POST HTTP Request
public interface APIService {
    @Headers(
            {
                "Content-Type:application/json",
                "Authorization: key=AAAAzz7fq5g:APA91bGh6uWHsNMAthwUIbB5P3smGLJk6UMbQ_uueTqjv_BDc2JbSBsRhq1eprSDruGU4MNm4NuqxiNk-uuxYfx5K567hxKv-7A_jbGgf5DC4leZc-FuqxZUqNDXOT291HpQB5mUTi5y"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
