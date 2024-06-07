package com.example.watch_hub.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class userProfileActivity extends AppCompatActivity {
    TextView textName, txtEmail, txtNumber, txtGender, txtLast;
    ProgressBar progressBar;
    String Name,LName,Email,Number,Gender;
    ImageView profile;
    Button delete;
    FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txtLast = findViewById(R.id.ShowLastName);
        txtGender = findViewById(R.id.ShowGenderAd);
        textName = findViewById(R.id.ShowFullName);
        txtEmail = findViewById(R.id.ShowEmailAd);
        txtNumber = findViewById(R.id.ShowPhoneAd);
        progressBar = findViewById(R.id.progressBar);

        delete = findViewById(R.id.deleteAccount);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userProfileActivity.this, Deleteaccount.class));
            }
        });

        // upload profile pics
        profile = findViewById(R.id.profilepic);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfileActivity.this, UploadImage.class);
                startActivity(intent);
                           }
        });



        Name = textName.getText().toString();
        Email = txtEmail.getText().toString();
        Number = txtNumber.getText().toString();
        Gender = txtGender.getText().toString();
        LName = txtLast.getText().toString();
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(this, "Something Went Wrong!. User Information Not Available", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            ShowUserProfile(firebaseUser);
        }
    }
    private void ShowUserProfile( FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Extracting User Info From Database

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null){
                    Name = firebaseUser.getDisplayName();
                    Email = firebaseUser.getEmail();
                    Gender = readWriteUserDetails.gender;
                    Number = readWriteUserDetails.mobile;
                    //Name = readWriteUserDetails.firstName;
                    LName = readWriteUserDetails.lastName;
                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.with(userProfileActivity.this).load(uri).into(profile);
                    String FullName = Name + " "+ LName;
                    textName.setText(FullName);
                    txtEmail.setText(Email);
                    txtGender.setText(Gender);
                    txtNumber.setText(Number);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userProfileActivity.this, "Something Went Wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}