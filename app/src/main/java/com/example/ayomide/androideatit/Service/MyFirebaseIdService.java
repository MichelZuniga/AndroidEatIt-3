package com.example.ayomide.androideatit.Service;

import com.example.ayomide.androideatit.Common.Common;
import com.example.ayomide.androideatit.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    FirebaseDatabase db;
    DatabaseReference tokens;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        db = FirebaseDatabase.getInstance();
        tokens = db.getReference("Tokens");
        Token token = new Token( tokenRefreshed, false ); //false because this token is sent from the client app
        tokens.child( Common.currentUser.getPhone()).setValue( token );
    }
}
