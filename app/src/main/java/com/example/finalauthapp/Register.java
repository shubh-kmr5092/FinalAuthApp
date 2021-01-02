package com.example.finalauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    Button regUser,cancel;
    private FirebaseAuth mAuth;
    EditText name,mail,password;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUser=(Button)findViewById(R.id.regUser);
        cancel=(Button)findViewById(R.id.cancel);
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        mail = (EditText)findViewById(R.id.mail);
        mAuth = FirebaseAuth.getInstance();
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);

        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String username=name.getText().toString().trim();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
                    mail.setError("Email is required");
                    password.setError("Password is required");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    mail.setError("Email is required");
                    return;
                } else if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is required");
                    return;
                }

               progressBar2.setVisibility(View.VISIBLE);
                registerUser(username,email,pass);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    void registerUser(String username,String email, String pass){
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();
                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",username);
                            hashMap.put("password",pass);

                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference=database.getReference("user");

                            reference.child(uid).setValue(hashMap);
                           //reference.child(email).setValue(hashMap);
                            startActivity(new Intent(getApplicationContext(),Hmi.class));
                            Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();

                            progressBar2.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), "User not registered!", Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);

                        }

                        // ...
                    }
                });

    }
}