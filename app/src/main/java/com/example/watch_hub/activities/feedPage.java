package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watch_hub.adapter.FeedAdapter;
import com.example.watch_hub.databinding.ActivityFeedPageBinding;
import com.example.watch_hub.domain.FeedModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class feedPage extends AppCompatActivity {
    ActivityFeedPageBinding binding;
    FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        feedAdapter = new FeedAdapter(this);

        // Setting click listeners for other activities
        binding.SupportPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(feedPage.this, supportPage.class));
            }
        });
        binding.categoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(feedPage.this, Categories.class));
            }
        });
        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(feedPage.this, HomePage.class));
            }
        });
        binding.profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(feedPage.this, userPage.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initFeed();
    }

    private void initFeed() {
        // Fetch feed items from Firestore
        FirebaseFirestore.getInstance().collection("feed").whereEqualTo("show", true)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<FeedModel> feedList = new ArrayList<>();
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            FeedModel feedModel = ds.toObject(FeedModel.class);
                            if (feedModel != null) {
                                feedList.add(feedModel);
                            }
                        }
                        // Add feed items to adapter
                        feedAdapter.addFeedList(feedList);
                        // Set adapter to RecyclerView
                        binding.feedDisplay.setLayoutManager(new LinearLayoutManager(feedPage.this));
                        binding.feedDisplay.setAdapter(feedAdapter);
                    }
                });
    }
}
