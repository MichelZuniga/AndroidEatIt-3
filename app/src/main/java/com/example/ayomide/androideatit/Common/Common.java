package com.example.ayomide.androideatit.Common;

import com.example.ayomide.androideatit.Model.User;

public class Common {
    //Variable to store current user
    public static User currentUser;

    public static String convertCodeToStatus(String status)
    {
        if(status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }
}
