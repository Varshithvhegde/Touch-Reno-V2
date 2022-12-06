package com.touchreno.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MyAcount extends AppCompatActivity {

    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth auth;

    FirebaseAuth firebaseAuth;
    TextView name,mobile,email,address,country,state;
    FirebaseDatabase database;
    LinearLayout home,cart,order,profile;
    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    String downloadUri;
    Button logout,edit;
    ProgressBar progressBar,progresssmall;
    ImageView profilimg,EditImage;
    String edtname,edtemail,edtmobile,edtaddress,edtcountry,edtstate;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acount);
        name=(EditText) findViewById(R.id.profile);
        email=(EditText) findViewById(R.id.email);
        mobile=(EditText) findViewById(R.id.mobile);
        address =(EditText) findViewById(R.id.address);
        country=(EditText) findViewById(R.id.country);
        state = (EditText) findViewById(R.id.state);
        logout=(Button) findViewById(R.id.logout);
        edit=(Button) findViewById(R.id.update);
        profilimg=(ImageView) findViewById(R.id.header);
         EditImage=(ImageView) findViewById(R.id.editImage);
        progressDialog=new ProgressDialog(this);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        progresssmall=(ProgressBar) findViewById(R.id.progress);
        progresssmall.setVisibility(View.GONE);
        home=findViewById(R.id.homenav);
        cart=findViewById(R.id.cartnav);
        order=findViewById(R.id.ordernav);
        profile=findViewById(R.id.profile1);
        database=FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        auth=FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseDatabase.getInstance();
        DatabaseReference ref= database.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.child(auth.getUid()).child("profimage").getValue(String.class);
                Glide.with(getApplicationContext())
                        .load(url) // image url
                        // any placeholder to load at start
                        .error(R.drawable.accessories)  // any image in case of error
                        // resizing

                        .into(profilimg);

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
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), myCartSys.class);
                startActivity(i);
//                ((Activity) getActivity()).overridePendingTransition(0, 0);


            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), myOrderSys.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               TO-DO
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();

                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);


                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().toString().equals("Edit")) {
                    EditDetails();
                }
                else{
                    UpdateDetails();
                    progresssmall.setVisibility(View.VISIBLE);
                }
            }
        });
        EditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });



        }

    private void uploadImage() {

        if (filePath != null) {

            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
            Toast.makeText(MyAcount.this, "Uploading Started", Toast.LENGTH_SHORT).show();
            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
//                                    String downloadUri = taskSnapshot.toStri  ng();
//                                    storage = FirebaseStorage.getInstance();
//                                    storageReference = storage.getReference();

//                                      downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult().toString();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            downloadUri = uri.toString();

                                        }
                                    });

                                    // Image uploaded successfully
                                    // Dismiss dialog
//                                    progressDialog.dismiss();
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(MyAcount.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Uploaddata();
                                        }
                                    }, 5 * 1000);

                                }

                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
//                            progressDialog.dismiss();
                            Toast
                                    .makeText(MyAcount.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                    progressDialog.show();
                                }
                            });
        }
        if(filePath==null){

            Toast
                    .makeText(MyAcount.this,
                            " Image  not Updated!",
                            Toast.LENGTH_SHORT)
                    .show();
            Uploaddatawithoutimage();
        }
    }

    private void Uploaddatawithoutimage() {
        if (TextUtils.isEmpty(edtcountry)) {
            Toast.makeText(getApplicationContext(), "Country is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtstate)) {
            Toast.makeText(getApplicationContext(), "State is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtmobile)) {
            Toast.makeText(getApplicationContext(), "Mobile is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtaddress)) {
            Toast.makeText(getApplicationContext(), "Address is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtname)) {
            Toast.makeText(getApplicationContext(), "Name is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtemail)) {
            Toast.makeText(getApplicationContext(), "Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference ref = database.getReference().child("Users").child(auth.getUid());
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("Country", edtcountry);
        updates.put("State", edtstate);
        updates.put("Mobile", edtmobile);
        updates.put("Address", edtaddress);

        updates.put("name",edtname);
        ref.updateChildren(updates);
        Toast.makeText(MyAcount.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
        progresssmall.setVisibility(View.INVISIBLE);
    }

    private void Uploaddata() {
        if (TextUtils.isEmpty(edtcountry)) {
            Toast.makeText(getApplicationContext(), "Country is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtstate)) {
            Toast.makeText(getApplicationContext(), "State is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtmobile)) {
            Toast.makeText(getApplicationContext(), "Mobile is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtaddress)) {
            Toast.makeText(getApplicationContext(), "Address is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtname)) {
            Toast.makeText(getApplicationContext(), "Name is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtemail)) {
            Toast.makeText(getApplicationContext(), "Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference ref = database.getReference().child("Users").child(auth.getUid());
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("Country", edtcountry);
        updates.put("State", edtstate);
        updates.put("Mobile", edtmobile);
        updates.put("Address", edtaddress);
        updates.put("profimage",downloadUri);
        updates.put("name",edtname);
        ref.updateChildren(updates);
        Toast.makeText(MyAcount.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
        progresssmall.setVisibility(View.INVISIBLE);
    }

    private void UpdateDetails() {
        uploadImage();
        name.setEnabled(false);

        mobile.setEnabled(false);
        country.setEnabled(false);
        address.setEnabled(false);
        state.setEnabled(false);
        edit.setText("Edit");
        EditImage.setVisibility(View.INVISIBLE);
        edtname=name.getText().toString();
        edtemail=email.getText().toString();
        edtmobile=mobile.getText().toString();
        edtcountry=country.getText().toString();
        edtaddress=address.getText().toString();
        edtstate=state.getText().toString();
    }

    private void EditDetails() {
        name.setEnabled(true);
       name.requestFocus();

       mobile.setEnabled(true);
       country.setEnabled(true);
       address.setEnabled(true);
       state.setEnabled(true);
        edit.setText("Update");
        EditImage.setVisibility(View.VISIBLE);

    }

    public void onBackPressed() {

        Intent a = new Intent(MyAcount.this,MainActivity.class);
        startActivity(a);
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                profilimg.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
}