package com.example.watch_hub.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watch_hub.adapter.ordersAdapter;
import com.example.watch_hub.databinding.ActivityOrdersPlacedBinding;
import com.example.watch_hub.domain.OrderModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ordersPlaced extends AppCompatActivity {
    private ordersAdapter ordersadapter;
    ActivityOrdersPlacedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersPlacedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getOrders();
        ordersadapter = new ordersAdapter(this);
        binding.ordersPlaced.setAdapter(ordersadapter);
        binding.ordersPlaced.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getOrders();
    }

    private void getOrders() {
        FirebaseFirestore.getInstance().collection("orders").whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            OrderModel orderModel = ds.toObject(OrderModel.class);
                            ordersadapter.addProduct(orderModel);
                        }
                    }
                });
    }
}