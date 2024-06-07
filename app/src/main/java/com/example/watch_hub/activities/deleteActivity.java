package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityDeleteaccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class deleteActivity extends AppCompatActivity {
ActivityDeleteaccountBinding binding;
FirebaseUser firebaseUser;
FirebaseAuth authProfile;
String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityDeleteaccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    authProfile = FirebaseAuth.getInstance();
    firebaseUser = authProfile.getCurrentUser();
binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (firebaseUser.equals("")){
            Toast.makeText(deleteActivity.this, "Something Went Wrong! User Details Are Not Available At " +
                    "The Moment", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(deleteActivity.this,MainActivity.class));
            finish();
        }else {
            reAuthenticateUser(firebaseUser);
            deleteUser(firebaseUser);
        }
    }
});


    }

    private void deleteUser(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                authProfile.signOut();
                Toast.makeText(deleteActivity.this, "User Has Been Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(deleteActivity.this,MainActivity.class));
                    finish();
            }
        });

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        email = binding.EmailAddress.getText().toString();
        password = binding.Pass.getText().toString();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(deleteActivity.this, "Enter Your Registered Email Address", Toast.LENGTH_SHORT).show();
            binding.EmailAddress.setError("Email Is required");
            binding.EmailAddress.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(deleteActivity.this, "Password Can't Be Empty", Toast.LENGTH_SHORT).show();
            binding.Pass.setError("Password Is required");
            binding.Pass.requestFocus();
        }
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),password);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(deleteActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}