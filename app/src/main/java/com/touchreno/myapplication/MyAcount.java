package com.touchreno.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAcount extends AppCompatActivity {
    TextView name,mobile,email,address,country,state;
    FirebaseDatabase database;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acount);
        name=(TextView) findViewById(R.id.profile);
        email=(TextView) findViewById(R.id.email);
        mobile=(TextView) findViewById(R.id.mobile);
        address =(TextView) findViewById(R.id.address);
        country=(TextView) findViewById(R.id.country);
        state = (TextView) findViewById(R.id.state);
        auth=FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseDatabase.getInstance();
        DatabaseReference ref= database.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child(auth.getUid()).child("name").getValue(String.class);
                String e = dataSnapshot.child(auth.getUid()).child("email").getValue(String.class);
                String m = dataSnapshot.child(auth.getUid()).child("Mobile").getValue(String.class);
                String a = dataSnapshot.child(auth.getUid()).child("Address").getValue(String.class);
                String c = dataSnapshot.child(auth.getUid()).child("Country").getValue(String.class);
                String s = dataSnapshot.child(auth.getUid()).child("State").getValue(String.class);
                name.setText(n);
                email.setText(e);
                mobile.setText(m);
                address.setText(a);
                country.setText(c);
                state.setText(s);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        }
}