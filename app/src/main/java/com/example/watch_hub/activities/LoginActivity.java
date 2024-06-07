package com.example.watch_hub.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailTXT, passwordTXT;

    TextView lostPass;
    ProgressBar progressBar;
    FirebaseAuth authProfile;
    Button toSignUP, Login;

    ImageView ShowHidePWD;
    private static final String TAG = "LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        ShowHidePWD = findViewById(R.id.passwordIcon);
        emailTXT = findViewById(R.id.emailLOG);
        passwordTXT = findViewById(R.id.passwordLOG);
        lostPass = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar);
        Login = findViewById(R.id.login);

//            getSupportActionBar().setTitle("LOGIN");
        authProfile = FirebaseAuth.getInstance();
        //SHOW HIDE PASSWORD
        ShowHidePWD.setImageResource(R.drawable.ic_show_pwd);
        ShowHidePWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordTXT.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    // IF PASSWORD IS VISIBLE THEN HIDE IT
                    passwordTXT.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // CHANGE ICON
                    ShowHidePWD.setImageResource(R.drawable.ic_show_pwd);
                }else {
                    passwordTXT.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ShowHidePWD.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        //LOGIN BTN
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = emailTXT.getText().toString();
                String textPassword = passwordTXT.getText().toString();
                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
                    emailTXT.setError("Email is Required");
                    emailTXT.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    passwordTXT.setError("Password is required");
                    passwordTXT.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUsers(textEmail,textPassword);
                }
            }
        });

        //Forgot Password
        lostPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        // TO SIGNUP PAGE
        toSignUP = findViewById(R.id.toSignup);
        toSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registration.class);
                startActivity(intent);
            }
        });


    }

    private void loginUsers(String Email, String Pword) {
        authProfile.signInWithEmailAndPassword(Email,Pword).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();

                    //GET INSTANCE OF THE CURRENT USER
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    //CHECK IF EMAIL IS VERIFIED
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        startActivity(intent);
                    }else {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }


                }else {
                    try {
                        throw task.getException();
                        // Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }catch (FirebaseAuthInvalidUserException e){
                        emailTXT.setError("User Can't be found");
                        emailTXT.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        emailTXT.setError("Invalid Credential. Kindly, Check And Re-Enter  ");
                        emailTXT.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        // setup alert builder

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("EMAIL NOT VERIFIED");
        builder.setMessage("Please Verify Your Email Address. You Can Not Login Without Email Verification");
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
    //Check if user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser()!= null){

            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "You Need To Sign In", Toast.LENGTH_SHORT).show();
        }
    }
}