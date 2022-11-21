package com.touchreno.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileDetail extends AppCompatActivity {
    Button sub;
    EditText ed,country,state,mobile,address;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        progressBar=(ProgressBar) findViewById(R.id.progressbar) ;
        progressBar.setVisibility(View.GONE);
        sub=(Button) findViewById(R.id.update);
        country=(EditText) findViewById(R.id.profile);
        state=(EditText) findViewById(R.id.email) ;
        mobile=(EditText) findViewById(R.id.mobile);
        address =(EditText) findViewById(R.id.address);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        Intent i=getIntent();
        String Final = i.getStringExtra("all");
        String str[] = Final.split("-?");

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String c = country.getText().toString();
                String s= state.getText().toString();
                String m= mobile.getText().toString();
                String a = address.getText().toString();
                if(TextUtils.isEmpty(c)){
                    Toast.makeText(getApplicationContext(), "Country is Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(s)){
                    Toast.makeText(getApplicationContext(), "State is Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(m)){
                    Toast.makeText(getApplicationContext(), "Mobile is Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(a)){
                    Toast.makeText(getApplicationContext(), "Address is Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference ref= database.getReference().child("Users").child(auth.getUid());
                Map<String, Object> updates = new HashMap<String,Object>();
                updates.put("Country",c);
                updates.put("State",s);
                updates.put("Mobile",m);
                updates.put("Address",a);

                ref.updateChildren(updates);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(ProfileDetail.this, "Details Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileDetail.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}