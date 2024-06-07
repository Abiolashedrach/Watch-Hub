package com.example.watch_hub.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.watch_hub.adapter.StoreAdapter;
import com.example.watch_hub.databinding.ActivityMain3Binding;
import com.example.watch_hub.domain.ProductModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class casualCategory extends AppCompatActivity {

    ActivityMain3Binding binding;
    StoreAdapter storeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storeAdapter = new StoreAdapter(this);
        initPopular();
        binding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initPopular() {
        // get product from firestore
        String VAL = "Casual Watches" ;
        FirebaseFirestore.getInstance().collection("products").whereEqualTo("category",VAL)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            ProductModel productModel = ds.toObject(ProductModel.class);
                            storeAdapter.addProduct(productModel);
                        }
                        binding.casual.setLayoutManager(new GridLayoutManager(casualCategory.this,2));
                        binding.casual.setAdapter(storeAdapter);

                    }

                });
    }
}
