package com.example.ayomide.androideatit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.graphics.Typeface;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnSignUp;
    TextView tvSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        tvSlogan = findViewById(R.id.tvSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/AlexBrush-Regular.ttf");
        tvSlogan.setTypeface(face);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

    }
        public void openActivity(){
            Intent signIn = new Intent(this, SignIn.class);
            startActivity(signIn);
        }

        public void openActivity2(){
            Intent signup = new Intent (this, SignUp.class);
            startActivity(signup);
        }
}
