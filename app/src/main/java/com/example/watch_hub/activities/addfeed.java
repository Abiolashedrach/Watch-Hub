package com.example.watch_hub.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityAddfeedBinding;
import com.example.watch_hub.domain.FeedModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addfeed extends AppCompatActivity {
    ActivityAddfeedBinding binding;

    private String Name;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddfeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.UploadFeedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = binding.feedName.getText().toString();
                if (uri != null) {
                    uploadImageAndAddProduct();
                } else {
                    Toast.makeText(addfeed.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void uploadImageAndAddProduct() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("feed/"+Name+".png");
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Update Firestore with image URI
                        FirebaseFirestore.getInstance().collection("feed").document(Name)
                                .set(new FeedModel(downloadUri.toString(), Name,true))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(addfeed.this, "Feed added successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(addfeed.this, "Failed to add feed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            binding.productImage.setImageURI(uri);
        }
    }
}
