 package com.example.ayomide.androideatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ayomide.androideatit.Common.Common;
import com.example.ayomide.androideatit.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.HashMap;

 public class SignUp extends AppCompatActivity {

     DatabaseReference table_user;

     String Security = "security code";

     MaterialEditText etPhone, etName, etPassword, etSecurityCode;
     Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etSecurityCode = findViewById(R.id.etSecurityCode);

        btnSignUp = findViewById(R.id.btnSignUp);

        //Init Firebase
        table_user = FirebaseDatabase.getInstance().getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isConnectedToTheInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog( SignUp.this );
                    mDialog.setMessage( "Calm down..." );
                    mDialog.show();

                    final String phone = etPhone.getText().toString();
                    final String name = etName.getText().toString();
                    final String password = etPassword.getText().toString();
                    final String securityCode = etSecurityCode.getText().toString();

                    if (TextUtils.isEmpty( phone ) || TextUtils.isEmpty( name ) || TextUtils.isEmpty( password )) {
                        Toast.makeText( SignUp.this, "All fields are required", Toast.LENGTH_SHORT ).show();
                    } else if (password.length() < 4) {
                        Toast.makeText( SignUp.this, "Password must be at least 4 characters", Toast.LENGTH_SHORT ).show();
                    } else if (securityCode.length() < 3) {
                        Toast.makeText( SignUp.this, "Security code must be at least 3 characters", Toast.LENGTH_SHORT ).show();
                    } else {
                        mDialog.dismiss();
                        table_user.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child( phone ).exists()) {
                                    mDialog.dismiss();
                                    Toast.makeText( SignUp.this, "this phone has been registered", Toast.LENGTH_SHORT ).show();
                                } else {
                                    User user = new User( name, password, phone, securityCode );
                                    table_user.child( phone ).setValue( user );
                                    Intent homeIntent = new Intent( SignUp.this, Home.class );
                                    Common.currentUser = user;
                                    startActivity( homeIntent );
                                    finish();
                                    final Toast toast = Toast.makeText( SignUp.this, "Please do not forget your "+Security.toUpperCase(),Toast.LENGTH_LONG );
                                    toast.show();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 10000);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );
                    }
                }
                else
                    Toast.makeText(SignUp.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
            }
        });
    }
 }
