package com.example.watch_hub.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.watch_hub.adapter.StoreAdapter;
import com.example.watch_hub.databinding.ActivitySearchViewBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class searchView extends AppCompatActivity {
StoreAdapter storeAdapter;

String searchInput;
ActivitySearchViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = ActivitySearchViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initPopular();
        searchInput = binding.SearchBar.getText().toString();
    }
    private void initPopular() {
        // get product from firestore
        CollectionReference productsRef = FirebaseFirestore.getInstance().collection("products");

        Query query = productsRef.whereGreaterThanOrEqualTo("title", searchInput)
                .whereLessThanOrEqualTo("title", searchInput);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    binding.popularProducts.setLayoutManager(new GridLayoutManager(searchView.this,2));
                    binding.popularProducts.setAdapter(storeAdapter);
                }
            });

    }
    }