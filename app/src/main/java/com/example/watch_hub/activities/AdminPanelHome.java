package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityAdminPanelHomeBinding;

public class AdminPanelHome extends AppCompatActivity {
ActivityAdminPanelHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminPanelHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanelHome.this, addProductAdmin.class);
                startActivity(intent);
            }
        });

        binding.addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanelHome.this,addfeed.class));
            }
        });

    }
}