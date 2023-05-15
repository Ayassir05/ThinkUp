package com.example.thinkup;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkup.modeldata.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegActivity extends AppCompatActivity {
    Button signUp;
    EditText name,email,password;
    TextView signIn;
    FirebaseAuth auth;

    FirebaseDatabase database;
    ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        progressBar = findViewById(R.id.prog1);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signUp = findViewById(R.id.btn_reg);
        name = findViewById(R.id.name_reg);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.pass_reg);
        signIn = findViewById(R.id.sign_in);

        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),logActivity.class);
            startActivity(intent);
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createUser() {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword=password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Name is Empty !",Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this,"Email is Empty !",Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Password is Empty !",Toast.LENGTH_SHORT).show();
            return;
        }if(userPassword.length() < 6 ){
            Toast.makeText(this,"Password Length must be greater then 6 letter !",Toast.LENGTH_SHORT).show();
            return;
        }
        //create a user
        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserModel userModel=new UserModel(userName,userEmail,userPassword);
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(id).setValue(userModel);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegActivity.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}