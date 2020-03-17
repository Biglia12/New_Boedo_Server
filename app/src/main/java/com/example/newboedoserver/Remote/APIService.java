package com.example.newboedoserver.Remote;

import com.example.newboedoserver.Model.MyResponse;
import com.example.newboedoserver.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAHEwOyeE:APA91bG0qBMv_qrv7oj96yUeEBK2ms836SrsQv0B7fpzChYJA6GltxpHvp4Z3YXw4IUVU_iaT6PWTgAWfixXFSfKKrko99Yl7Q6FaXawJtDCNOpLRKEFwGo5OwteH5x4LUcJpKZXAQ-c"

            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
