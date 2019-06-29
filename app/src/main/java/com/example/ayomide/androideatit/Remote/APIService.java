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
                "Authorization: key=AAAA66K7Otg:APA91bEFTSxab5Te_6FrVDw91VjOgGCHqEWpdil5bde71JthF_P6hsNfVJch-iKs3WOnXNp2k-y8JWK2MXF2quLaI46okwGOcEYFbUU0ICvt73TJD_eQMjhk7vr8Oq44vA7ltVEngEXP3MU-Lm1O_pdfHjzt5800xA"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
