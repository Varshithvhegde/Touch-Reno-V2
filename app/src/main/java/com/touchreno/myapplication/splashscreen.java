package com.touchreno.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashscreen extends AppCompatActivity {
    FirebaseAuth auth;
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splashscreen);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // Start home activity
                    startActivity(new Intent(splashscreen.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(splashscreen.this, LoginActivity.class));
                    finish();
                }
            }
        },SPLASH_SCREEN_TIME_OUT);
    }
}