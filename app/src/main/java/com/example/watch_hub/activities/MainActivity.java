package com.example.watch_hub.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.R;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth authProfile;
    private static final String TAG = "LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        authProfile = FirebaseAuth.getInstance();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser()!= null){
            Thread screen = new Thread(){
                public void run() {
                    try {
                        int delay = 0;
                        while (delay < 1000){
                            sleep(15);
                            delay = delay+5;
                        }
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                    }catch (InterruptedException ex){
                        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        finish();
                    }

                }
            };
            screen.start();


        } else if (authProfile.getCurrentUser() == null){

                Toast.makeText(this, "Sign up", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,splash.class);
                startActivity(intent);
                finish();

        } else {
            Intent intent = new Intent(MainActivity.this, splash.class);
            startActivity(intent);
            Toast.makeText(this, "You Need To Sign In", Toast.LENGTH_SHORT).show();
        }
    }
}