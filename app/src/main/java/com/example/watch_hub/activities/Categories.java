package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityCategoriesBinding;

public class Categories extends AppCompatActivity {
    ActivityCategoriesBinding binding;

    String smart,casual,high_end,luxury,ladies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        smart = binding.smartWatches.getText().toString();
        casual = binding.casualWatches.getText().toString();
        high_end = binding.highEndWatches.getText().toString();
        luxury = binding.luxuryWatch.getText().toString();
        ladies = binding.ladiesWatches.getText().toString();


        binding.casualWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this, casualCategory.class);
                startActivity(intent);
                 }
        });
        binding.ladiesWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this, ladiesWag.class);
                startActivity(intent);
            }
        });
        binding.smartWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this, smartWatch.class);
                startActivity(intent);
            }
        });
        binding.luxuryWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this, luxury.class);
                startActivity(intent);
            }
        });
        binding.highEndWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Categories.this, highendWatches.class);
                startActivity(intent);
            }
        });

        binding.SupportPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Categories.this, supportPage.class));
            }
        });
        binding.feedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Categories.this, feedPage.class));
            }
        });

        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Categories.this, HomePage.class));
            }
        });
        binding.profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Categories.this, userPage.class));
            }
        });

    }
}