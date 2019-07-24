package com.example.ayomide.androideatit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayomide.androideatit.Common.Common;
import com.example.ayomide.androideatit.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    MaterialEditText etPhone, etPassword;
    TextView tvForgotPass;
    Button btnSignIn;
    CheckBox cbRemember;

    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        cbRemember = findViewById(R.id.cbRemember);

        //init Paper
        Paper.init(this);

        //Init Firebase
        table_user = FirebaseDatabase.getInstance().getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIntoHomeActivity();
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void signIntoHomeActivity()
    {
        if (Common.isConnectedToTheInternet(getBaseContext())) {
            //function to save user & password is checkbox is checked
            if(cbRemember.isChecked())
            {
                Paper.book().write( Common.USER_KEY, etPhone.getText().toString() );
                Paper.book().write( Common.PWD_KEY, etPassword.getText().toString() );
            }

            final ProgressDialog mDialog = new ProgressDialog( SignIn.this );
            mDialog.setMessage( "Calm down..." );
            mDialog.show();

            if (TextUtils.isEmpty( etPhone.getText().toString() )) {
                Toast.makeText( SignIn.this, "Please enter phone number...", Toast.LENGTH_SHORT ).show();
            }
            if (TextUtils.isEmpty( etPassword.getText().toString() )) {
                Toast.makeText( SignIn.this, "Please enter password...", Toast.LENGTH_SHORT ).show();
            } else {
                table_user.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check if user exists in database
                        if (dataSnapshot.child( etPhone.getText().toString() ).exists()) {
                            //Get User info
                            mDialog.dismiss();

                            User user = dataSnapshot.child( etPhone.getText().toString() ).getValue( User.class );

                            if (user.getPassword().equals( etPassword.getText().toString() )) {

                                Intent homeIntent = new Intent( SignIn.this, Home.class );
                                Common.currentUser = user;
                                startActivity( homeIntent );
                                finish();
                                Toast.makeText( SignIn.this, "Login successful", Toast.LENGTH_LONG ).show();

                            } else {
                                Toast.makeText( SignIn.this, "Incorrect Password", Toast.LENGTH_LONG ).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText( SignIn.this, "Baba, you no get account", Toast.LENGTH_LONG ).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        else
            Toast.makeText(SignIn.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        return;
    }

    private void showDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your security code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password_layout, null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final MaterialEditText etPhone = forgot_view.findViewById(R.id.etPhone);
        final MaterialEditText etSecurityCode = forgot_view.findViewById(R.id.etSecurityCode);

        builder.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Check if user is available
                table_user.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(etPhone.getText().toString()).getValue(User.class);

                        if(user.getSecurityCode().equals(etSecurityCode.getText().toString()))
                            Toast.makeText(SignIn.this, "Your password is: "+user.getPassword(), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SignIn.this, "Wrong security code", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        builder.setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
