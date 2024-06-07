package com.example.watch_hub.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityUserSettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userSettings extends AppCompatActivity {
ActivityUserSettingsBinding binding;

String oldEmail,newEmail,oldPassword,newPassword;
Button changeEmail,changePassword;
FirebaseAuth authProfile;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        FName = binding.FirstName.getText().toString();
//        LName =  binding.LastName.getText().toString();
//        EMail = binding.Email.getText().toString();
//        NUmber = binding.PhoneNumber.getText().toString();


        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        oldEmail = firebaseUser.getEmail();

        binding.OldEmail.setText(oldEmail);

        if (firebaseUser.equals("")){
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
        binding.updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEmail = binding.newEmail.getText().toString();
                if (TextUtils.isEmpty(newEmail)){
                    Toast.makeText(userSettings.this, "Enter Your New Email Address", Toast.LENGTH_SHORT).show();
                   binding.newEmail.setError("Email Address Is required");
                    binding.newEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    Toast.makeText(userSettings.this, "Please Re-Enter Your Email Address", Toast.LENGTH_SHORT).show();
                    binding.newEmail.setError("Input a valid email address");
                    binding.newEmail.requestFocus();
                } else if (oldEmail.matches(newEmail)) {
                    Toast.makeText(userSettings.this, "New Email Address Can't Be the same as old one", Toast.LENGTH_SHORT).show();
                    binding.newEmail.setError("Email Address Is required");
                    binding.newEmail.requestFocus();
                }else {
                    updateEmail(firebaseUser);
                }


            }
        });

        binding.updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword = binding.newPass.getText().toString();
                if (TextUtils.isEmpty(newEmail)){
                    Toast.makeText(userSettings.this, "Enter Your New Password", Toast.LENGTH_SHORT).show();
                    binding.newPass.setError("Password Is required");
                    binding.newPass.requestFocus();
                } else if (oldPassword.matches(newPassword)) {
                    Toast.makeText(userSettings.this, "New Password Can't Be the same as old one", Toast.LENGTH_SHORT).show();
                    binding.newEmail.setError("Password Is required");
                    binding.newEmail.requestFocus();
                }else {
                    updatePassword(firebaseUser);
                }
            }
        });

    }

    private void updatePassword(FirebaseUser firebaseUser) {
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                firebaseUser.sendEmailVerification();
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(userSettings.this, "Email Changed Successfully Check Email", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


}