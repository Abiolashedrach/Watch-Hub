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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    EditText fName,lName,Email,Number,Password,ConPassword;
    RadioGroup radioGroup;
        Button signup;
    ProgressBar progressBar;

    RadioButton genderSelected;
    final static String TAG = "RegisterActivity";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get all views by the id name
        fName = findViewById(R.id.FirstName);
        lName = findViewById(R.id.LastName);
        Email = findViewById(R.id.Email);
        Number = findViewById(R.id.PhoneNumber);
        Password = findViewById(R.id.Password);
        ConPassword = findViewById(R.id.conPassword);
        radioGroup = findViewById(R.id.GendeR);
        radioGroup.clearCheck();
        signup = findViewById(R.id.register);
            progressBar  = findViewById(R.id.progressBar);
        // on click function for the register button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for the radio button id from the group
                int genderID = radioGroup.getCheckedRadioButtonId();
                genderSelected = findViewById(genderID);

                //obtaining entered data

                String First = fName.getText().toString().trim();
                String Last = lName.getText().toString().trim();
                String emailAD = Email.getText().toString().trim();
                String Phone = Number.getText().toString().trim();
                String Pass = Password.getText().toString().trim();
                String CPass = ConPassword.getText().toString().trim();
                String Genders; // can't input any value without confirm if any button been clicked
                // checking for button clicked
                if (TextUtils.isEmpty(First)) {
                    Toast.makeText(Registration.this, "Enter Your First Name", Toast.LENGTH_SHORT).show();
                    fName.setError("First Name Is required");
                    fName.requestFocus();
                } else if (TextUtils.isEmpty(Last)) {
                    Toast.makeText(Registration.this, "Enter Your Last Name", Toast.LENGTH_SHORT).show();
                    lName.setError("Last Name Is required");
                    lName.requestFocus();
                } else if (TextUtils.isEmpty(emailAD)) {
                    Toast.makeText(Registration.this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
                    Email.setError("Email Address Is required");
                    Email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAD).matches()) {
                    Toast.makeText(Registration.this, "Please Re-Enter Your Email Address", Toast.LENGTH_SHORT).show();
                    Email.setError("Input a valid email address");
                    Email.requestFocus();
                } else if (TextUtils.isEmpty(Phone)) {
                    Toast.makeText(Registration.this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                    Number.setError("Phone Number Is required");
                    Number.requestFocus();
                } else if (TextUtils.isEmpty(Pass)) {
                    Toast.makeText(Registration.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
                    Password.setError("Password Is required");
                    Password.requestFocus();
                }else if (TextUtils.isEmpty(CPass)){
                    Toast.makeText(Registration.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
                    Password.setError("Password Is required");
                    Password.requestFocus();
                } else if (Pass.length() < 6) {
                    Toast.makeText(Registration.this, "Password Should Not Be Less Than 6", Toast.LENGTH_SHORT).show();
                    Password.setError("Password Is Short");
                    Password.requestFocus();
                } else if(radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Registration.this, "Select Gender", Toast.LENGTH_SHORT).show();
                    genderSelected.setError("field required");
                    genderSelected.requestFocus();

                }else {
                    Genders =  genderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(emailAD,Pass).addOnCompleteListener(Registration.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser firebaseUser = task.getResult().getUser();

                                        // update display name of user
                                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(First).build();
                                        if (firebaseUser != null) {
                                            firebaseUser.updateProfile(userProfileChangeRequest);
                                        }

                                        // Enter User Data in to the firebase real time Database
                                        ReadWriteUserDetails readWriteUserDetails = new ReadWriteUserDetails(First,Last,Genders,Phone);

                                        // EXTRACTING USER REFERENCE FROM DATABASE FOR "REGISTERED USERS "
                                        DatabaseReference referenceProfile =  FirebaseDatabase.getInstance().getReference().child("Registered Users");
                                        referenceProfile.child(firebaseUser.getUid()).setValue(readWriteUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                                if (task.isSuccessful()) {
                                                    //SEND VERIFICATION EMAIL
                                                    Toast.makeText(Registration.this, "User Registered Successfully Please Verify Your Email", Toast.LENGTH_SHORT).show();

                                                    firebaseUser.sendEmailVerification();

                                                    //intent listerner

                                                    Intent intent = new Intent(Registration.this, HomePage.class);
                                                    // clears backlog of activities preventing user from going back to registration page after successful sign up
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish(); //TO CLOSE REGISTER ACTIVITY
                                                }else {
                                                    Toast.makeText(Registration.this, "User Registered Failed", Toast.LENGTH_SHORT).show();
                                                }
                                                progressBar.setVisibility(View.GONE);

                                            }

                                        });

                                    }else {
                                        try {
                                            throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        Password.setError("Email Already In Use");
                        Password.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                       Email.setError("User Already Exists");
                       Email.requestFocus();
                        Toast.makeText(Registration.this, e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                        }catch (Exception e){
                                            Log.e(TAG, e.getMessage());
                                            Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                }
            }
        });


    }

//    private void registerUser(String First, String Last, String emailAD,String Genders, String Phone, String Pass) {
//
//
//      //CREATE USER PROFILE
//
//    }
}