package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watch_hub.adapter.CartAdapter;
import com.example.watch_hub.databinding.ActivityCartBinding;
import com.example.watch_hub.domain.CartModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CartActivity extends AppCompatActivity {
ActivityCartBinding binding;
private CartAdapter cartAdapter;
public static List<CartModel> cartModelList;
    int mainTotal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cartAdapter = new CartAdapter(this);
            getCart();

            binding.checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartModelList = cartAdapter.getSelectedItems();
                    Intent intent = new Intent(CartActivity.this, OrderPlacing.class);
                    startActivity(intent);
                }
            });

    }

    private void getCart() {
        FirebaseFirestore.getInstance().collection("cart").whereEqualTo("sellerUid", FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot ds : dsList){
                    binding.checkOutList.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    binding.checkOutList.setAdapter(cartAdapter);
                    CartModel cartModel = ds.toObject(CartModel.class);
                    cartAdapter.addProduct(cartModel);

                }
            }
        });
    }

    private void decreaseQuantity(TextView quantityTextView) {
        int quantity = Integer.parseInt(quantityTextView.getText().toString());
        if (quantity > 0) {
            quantity--;
            quantityTextView.setText(String.valueOf(quantity));
        }
    }

    private void increaseQuantity(TextView quantityTextView) {
        int quantity = Integer.parseInt(quantityTextView.getText().toString());
        quantity++;
        quantityTextView.setText(String.valueOf(quantity));
    }
}