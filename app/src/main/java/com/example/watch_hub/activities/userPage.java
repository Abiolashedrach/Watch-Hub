package com.example.watch_hub.activities;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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

public class userPage extends AppCompatActivity {
    TextView textInfo, txtWelcome, txtSettings, txtOrders, txtWishList, txtAddress;
    ProgressBar progressBar;
    String welcome,info,setttings,orders,wishList,address;

    Button logOut;
    ImageView profilep,backBTN,home,categories,feed,support;
    FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        profilep = findViewById(R.id.profilePic);
        logOut = findViewById(R.id.LogOut);
        feed = findViewById(R.id.feedPage);
        support = findViewById(R.id.SupportPage);
        txtWelcome = findViewById(R.id.show_Welcome);
        textInfo = findViewById(R.id.ShowAccountInfo);
            progressBar = findViewById(R.id.progressBar);
            backBTN = findViewById(R.id.backBTN);
        txtOrders = findViewById(R.id.Order);
        welcome = txtWelcome.getText().toString();

        home = findViewById(R.id.imageView5);

           support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(userPage.this, supportPage.class));
                }
            });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userPage.this, feedPage.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userPage.this, HomePage.class));
            }
        });

        categories = findViewById(R.id.categoryPage);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userPage.this, Categories.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                finish();
                startActivity(new Intent(userPage.this,MainActivity.class));

            }
        });


        textInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userPage.this, userProfileActivity.class);
                startActivity(intent);
            }
        });
//
       txtOrders.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(userPage.this, ordersPlaced.class);
               startActivity(intent);
           }
       });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();



        if (firebaseUser == null){
            Toast.makeText(this, "Something Went Wrong!. User Information Not Available", Toast.LENGTH_SHORT).show();
        }else{
            CheckIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            ShowUserProfile(firebaseUser);


        }

    }

    private void CheckIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
          showAlertDialog();
        }  else{
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog() {
        // setup alert builder

        AlertDialog.Builder builder = new AlertDialog.Builder(userPage.this);
        builder.setTitle("EMAIL NOT VERIFIED");
        builder.setMessage("Please Verify Your Email Address. You Can Not Continue Without Email Verification");
        //OPEN EMAIL APP IF USER CLICKS CONTINUE
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // TO EMAIL APP IN NEW WINDOW AND NOT WITHIN THE APP
                startActivity(intent);
            }
        });
        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show The Alert Dialog

        alertDialog.show();

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
                    welcome = firebaseUser.getDisplayName();
                    txtWelcome.setText("Welcome "+welcome);

                    //Set Dp After user Has Uploaded

                    Uri uri = firebaseUser.getPhotoUrl();

                    //Set ImageViewer Uri

                    Picasso.with(userPage.this).load(uri).into(profilep);

                }else{
                    Toast.makeText(userPage.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userPage.this, "Something Went Wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}