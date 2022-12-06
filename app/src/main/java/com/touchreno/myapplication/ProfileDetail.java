package com.touchreno.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.touchreno.myapplication.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileDetail extends AppCompatActivity {
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    Button sub;
    String downloadUri;
    EditText ed,country,state,mobile,address;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    ImageView profileimg;
    ProgressDialog progressDialog;
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
        profileimg=(ImageView) findViewById(R.id.header);
        progressDialog=new ProgressDialog(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        Intent i=getIntent();
        String Final = i.getStringExtra("all");
        String str[] = Final.split("-?");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
//                uploadImage();
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                progressBar.setVisibility(View.VISIBLE);
            }

        });
    }

    private void Uploaddata() {
        String c = country.getText().toString();
        String s = state.getText().toString();
        String m = mobile.getText().toString();
        String a = address.getText().toString();
        if (TextUtils.isEmpty(c)) {
            Toast.makeText(getApplicationContext(), "Country is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(getApplicationContext(), "State is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(m)) {
            Toast.makeText(getApplicationContext(), "Mobile is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(a)) {
            Toast.makeText(getApplicationContext(), "Address is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = database.getReference().child("Users").child(auth.getUid());
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("Country", c);
        updates.put("State", s);
        updates.put("Mobile", m);
        updates.put("Address", a);
        updates.put("profimage",downloadUri);
        ref.updateChildren(updates);
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(ProfileDetail.this, "Details Uploaded Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ProfileDetail.this, MainActivity.class);
        startActivity(i);
    }

    private void uploadImage() {

        if (filePath != null) {

            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
            Toast.makeText(ProfileDetail.this, "Uploading Started", Toast.LENGTH_SHORT).show();
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
                                            .makeText(ProfileDetail.this,
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
                                    .makeText(ProfileDetail.this,
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
            downloadUri="";
            Toast
                    .makeText(ProfileDetail.this,
                            "Profile Image is not selected!",
                            Toast.LENGTH_SHORT)
                    .show();
            Uploaddata();
        }
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
                profileimg.setImageBitmap(bitmap);
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