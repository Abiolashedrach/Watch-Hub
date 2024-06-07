package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.watch_hub.adapter.CategoryAdapter;
import com.example.watch_hub.adapter.SliderAdapter;
import com.example.watch_hub.adapter.StoreAdapter;
import com.example.watch_hub.databinding.ActivityHomePageBinding;
import com.example.watch_hub.domain.CategoryDomain;
import com.example.watch_hub.domain.ProductModel;
import com.example.watch_hub.domain.SliderItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BaseActivity {

ActivityHomePageBinding binding;

StoreAdapter storeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storeAdapter = new StoreAdapter(this);
        binding.SearchBar.clearFocus();

        initBanner();
        initCategory();
        initPopular();

        binding.profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, userPage.class);
                startActivity(intent);
            }
        });

        binding.categoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,Categories.class);
                startActivity(intent);
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, CartActivity.class);
                startActivity(intent);
            }
        });
        binding.SupportPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, supportPage.class));
            }
        });
        binding.feedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, feedPage.class));
            }
        });



        binding.SearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, searchView.class));
            }
        });



    }



    private void initPopular() {
        // get product from firestore
        FirebaseFirestore.getInstance().collection("products").whereEqualTo("show",true)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                           ProductModel productModel = ds.toObject(ProductModel.class);
                           storeAdapter.addProduct(productModel);
                        }
                        binding.popularProducts.setLayoutManager(new GridLayoutManager(HomePage.this,2));
                        binding.popularProducts.setAdapter(storeAdapter);
                         binding.progressBarPopular.setVisibility(View.GONE);
                    }

                });


/*//get products from database
//        DatabaseReference myDbRef = firebaseDatabase.getReference("Items");
//        binding.progressBarPopular.setVisibility(View.VISIBLE);
//        ArrayList<itemDomain> items = new ArrayList<>();
//        myDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        items.add(issue.getValue(itemDomain.class));
//                    }
//                    if (!items.isEmpty()) {
//                        binding.popularProducts.setLayoutManager(new GridLayoutManager(HomePage.this,2));
//                        binding.popularProducts.setAdapter(new PopularAdapter(items));
//                    }
//
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });*/
    }

    private void initCategory() {
    DatabaseReference myDbRef = firebaseDatabase.getReference("Category");
    binding.progressBarBrands.setVisibility(View.VISIBLE);
    ArrayList<CategoryDomain> items = new ArrayList<>();
    myDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for (DataSnapshot issue : snapshot.getChildren()) {
                    items.add(issue.getValue(CategoryDomain.class));
                }
                if (!items.isEmpty()) {
                    binding.BrandLogo.setLayoutManager(new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false));
                    binding.BrandLogo.setAdapter(new CategoryAdapter(items));
                }
                binding.progressBarBrands.setVisibility(View.GONE);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    private void category(ArrayList<CategoryDomain> items) {
    }

    private void initBanner() {
        DatabaseReference dbRef = firebaseDatabase.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItem> items= new ArrayList<>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                    items.add(issue.getValue(SliderItem.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItem> items) {
        binding.banners.setAdapter(new SliderAdapter(items, binding.banners));
        binding.banners.setClipToPadding(false);
        binding.banners.setClipChildren(false);
        binding.banners.setOffscreenPageLimit(3);
        binding.banners.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));

        binding.banners.setPageTransformer(compositePageTransformer);
    }
}