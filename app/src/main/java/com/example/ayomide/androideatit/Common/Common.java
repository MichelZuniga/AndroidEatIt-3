package com.example.ayomide.androideatit.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.example.ayomide.androideatit.Model.User;
import com.example.ayomide.androideatit.Remote.APIService;
import com.example.ayomide.androideatit.Remote.RetrofitClient;

import retrofit2.Retrofit;

public class Common {
    //Variable to store current user
    public static User currentUser;

    public static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService()
    {
        return RetrofitClient.getClient( BASE_URL ).create( APIService.class );
    }

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static boolean isConnectedToTheInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i=0;i<info.length;i++)
                {
                    if(info[i].getState().equals(NetworkInfo.State.CONNECTED))
                        return true;
                }
            }
        }
        return false;
    }

    public static String convertCodeToStatus(String status)
    {
        if(status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
            return "Delivered";
    }
}
