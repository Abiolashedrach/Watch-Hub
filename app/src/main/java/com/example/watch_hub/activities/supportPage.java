package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivitySupportPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class supportPage extends AppCompatActivity {
ActivitySupportPageBinding binding;
String name,email,complaint;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupportPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        binding.email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        binding.categoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(supportPage.this, Categories.class));
            }
        });
        binding.feedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(supportPage.this, feedPage.class));
            }
        });

        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(supportPage.this, HomePage.class));
            }
        });
        binding.profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(supportPage.this, userPage.class));
            }
        });
        binding.submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
                finish();
            }
        });
    }

    private void addProduct() {



       name = binding.Name.getText().toString();
       email = binding.email.getText().toString();
       complaint = binding.Complaint.getText().toString();

        ReadWriteComplaintsDetails readWriteUserDetails = new ReadWriteComplaintsDetails( name,email,complaint);

        // EXTRACTING USER REFERENCE FROM DATABASE FOR "REGISTERED USERS "
        DatabaseReference referenceProfile =  FirebaseDatabase.getInstance().getReference().child("complaint");
        referenceProfile.child(firebaseUser.getUid()).setValue(readWriteUserDetails);

    }
}