package com.touchreno.myapplication;

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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    Button send;
    EditText email;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        send=(Button) findViewById(R.id.send);
        email=(EditText) findViewById(R.id.email);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String e= email.getText().toString();
                if(TextUtils.isEmpty(e)) {
                    Toast.makeText(ForgotPassword.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(e)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ForgotPassword.this, "Reset Link is sent to your mail(Check in Spam)", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ForgotPassword.this, "Enter a Proper Email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                }

        });
    }
}