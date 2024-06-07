package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityAdminpanelLogBinding;

public class adminpanelLOG extends AppCompatActivity {
ActivityAdminpanelLogBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminpanelLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.emailLOGAD.getText().toString().equals("WatchHubAdmin") && binding.passwordLOGAD.getText().toString().equals("EPRO12345")){
                    Intent intent = new Intent(adminpanelLOG.this, AdminPanelHome.class);
                    startActivity(intent);

                }            }
        });
    }
}