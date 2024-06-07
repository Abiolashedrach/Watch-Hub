package com.example.watch_hub.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.watch_hub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadImage extends AppCompatActivity {
Button chooseIMG, uploadIMG;
ImageView profile;

ProgressBar progressBar;
FirebaseAuth firebaseAuth;
StorageReference storageReference;
FirebaseUser firebaseUser;
private static final int PICK_IMAGE_REQUEST = 1;
Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chooseIMG = findViewById(R.id.chooseImageBTN);
        uploadIMG = findViewById(R.id.UploadImageBTN);
        profile = findViewById(R.id.imageProfile);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("ProfilePics");

        Uri uri = firebaseUser.getPhotoUrl();

        // Set User Current pics in ImageView (if uploaded Already) using Picasso Library

        Picasso.with(UploadImage.this).load(uri).into(profile);
        // CHOOSE IMAGE BTN
        chooseIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // UPLOAD IMAGE BTN
        uploadIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });

    }

    private void UploadPic() {
    if (uriImage != null){
        // Save Image With UID Of The Currently Logged User
        StorageReference fileReference = storageReference.child(firebaseAuth.getCurrentUser().getUid() + "." + getFileExtention(uriImage));

        //Upload The Image To Storage

         fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         Uri downloadUri = uri;
                         firebaseUser = firebaseAuth.getCurrentUser();

                         // Set Display Image After User Uploads

                         UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                        firebaseUser.updateProfile(profileUpdates);
                     }
                 });
                 progressBar.setVisibility(View.GONE);
                 Toast.makeText(UploadImage.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(UploadImage.this, userPage.class);
                 startActivity(intent);
                 finish();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(UploadImage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });

    }else {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(UploadImage.this, "No File Selected", Toast.LENGTH_SHORT).show();
    }
    }

    //File Directory of the File
    private String getFileExtention(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //File Chooser
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    //Set Pics
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode  == RESULT_OK && data != null && data.getData() != null){
            uriImage = data.getData();
            profile.setImageURI(uriImage);
        }
    }
}