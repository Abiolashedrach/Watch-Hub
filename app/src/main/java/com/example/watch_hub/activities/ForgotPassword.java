package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.watch_hub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPassword extends AppCompatActivity {
        Button resetPWD;
        EditText resetTEXT;
        ProgressBar progressBar;
        FirebaseAuth authProfile;
    final static String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        resetPWD = findViewById(R.id.passwordReset);
        resetTEXT = findViewById(R.id.emailField);
        progressBar = findViewById(R.id.progressBar);
        resetPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetTEXT.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPassword.this, "Please Enter Registered Email", Toast.LENGTH_SHORT).show();
                    resetTEXT.setError("Email Is Required");
                    resetTEXT.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPassword.this, "Email Invalid", Toast.LENGTH_SHORT).show();
                    resetTEXT.setError("Email Invalid");
                    resetTEXT.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);

                }            }
        });

    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){
                 Toast.makeText(ForgotPassword.this, "Please Check Email For Password Reset Link", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intent);
                 finish();
             }else {
                 try {
                     throw  task.getException();
                 }catch (FirebaseAuthInvalidUserException firebaseAuthInvalidUserException){
                     resetTEXT.setError("User Does Not Exists");
                     resetTEXT.requestFocus();
                 }catch (Exception e){
                     Log.e(TAG,e.getMessage());
                     Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }
            }
        });
    }
}