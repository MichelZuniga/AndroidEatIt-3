 package com.example.ayomide.androideatit;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;

 public class SignUp extends AppCompatActivity {

     DatabaseReference table_user;

     MaterialEditText etPhone, etName, etPassword;
     Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);

        btnSignUp = findViewById(R.id.btnSignUp);

        //Init Firebase
        table_user = FirebaseDatabase.getInstance().getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Calm down...");
                mDialog.show();

                final String phone = etPhone.getText().toString();
                final String name = etName.getText().toString();
                final String password = etPassword.getText().toString();

                if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignUp.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if (password.length() < 6)
                {
                    Toast.makeText(SignUp.this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mDialog.dismiss();
                    table_user.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(phone).exists())
                            {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "this phone has been registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                User user = new User(name, password, phone);
                                table_user.child(phone).setValue(user);
                                Intent homeIntent = new Intent(SignUp.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                                Toast.makeText( SignUp.this, "welcome o", Toast.LENGTH_LONG ).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
 }
