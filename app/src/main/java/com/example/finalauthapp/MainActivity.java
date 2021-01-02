package com.example.finalauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button register,reset,login;
    EditText edit_user,edit_pass;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=(Button)findViewById(R.id.login);
        reset=(Button)findViewById(R.id.reset);
        register=(Button)findViewById(R.id.register);
        edit_user=(EditText)findViewById(R.id.edit_user);
        edit_pass= (EditText)findViewById(R.id.edit_pass);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edit_user.getText().toString().trim();
                String password=edit_pass.getText().toString().trim();
                loginUser(email,password);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_pass.getText().clear();
                edit_user.setText("");
            }
        });
    }

    void loginUser(String email, String password){

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            edit_user.setError("Email is required");
            edit_pass.setError("Password is required");
            return;
        }
        else if(TextUtils.isEmpty(email))
        {
            edit_user.setError("Email is required");
            return;
        }

        else if(TextUtils.isEmpty(password))
        {
            edit_pass.setError("Password is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Authenticate the user

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Toast.makeText(LoginActivity.this, "LoggedIn", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    savedata(email);

                    //startActivity(new Intent(getApplicationContext(),Hmi.class));
                    //finish();
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Error! " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    void savedata(String email){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("logincounter",true);
        editor.putString("useremail",email);
        editor.apply();
        startActivity(new Intent(getApplicationContext(),Hmi.class));
        finish();
    }
}