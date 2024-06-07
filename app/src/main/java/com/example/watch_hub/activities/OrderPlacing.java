package com.example.watch_hub.activities;

import static com.example.watch_hub.activities.CartActivity.cartModelList;
import static com.example.watch_hub.activities.ForgotPassword.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityOrderPlacingBinding;
import com.example.watch_hub.domain.CartModel;
import com.example.watch_hub.domain.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.UUID;

public class OrderPlacing extends AppCompatActivity {
ActivityOrderPlacingBinding binding;
private String name,number,address,country;
int mainTotal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderPlacingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.customerName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


        binding.confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.customerName.getText().toString();
                number = binding.contactInfo.getText().toString();
                address = binding.deliveryAddress.getText().toString();
                country = binding.country.getText().toString();
                placeOrder();
                deleteFromCart();
            }
        });

    }

    private void deleteFromCart() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartRef = db.collection("cart");

        cartRef.whereEqualTo("sellerUid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Delete each document representing an item in the user's cart
                                db.collection("cart").document(FirebaseAuth.getInstance().getUid()).delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        int subtotal = 0;
        int tax = 0;


        for (CartModel cartModel : cartModelList) {
            int price = Integer.parseInt(cartModel.getProductPrice());
            int qty = Integer.parseInt(cartModel.getProductQuantity());
            subtotal += price * qty;
        }

        tax = (int) (subtotal * 0.1); // Assuming 10% tax
        mainTotal = subtotal + tax + 195; // Assuming a fixed additional charge

        binding.productPrice.setText(String.valueOf(subtotal));
        binding.tax.setText(String.valueOf(tax));
        binding.deliveryFee.setText("$195");
        binding.total.setText(String.valueOf(mainTotal));
    }


    private void placeOrder() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Placing Order");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String orderNumber = String.valueOf(generateRandomNumber(100000, 999999));
        String tracking = String.valueOf(generateRandomNumber(500000, 300000));
        OrderModel orderModel = new OrderModel(orderNumber, name, address, country, number,
                String.valueOf(mainTotal), "195", tracking, "DHL",
                String.valueOf(Calendar.getInstance().getTimeInMillis()), "Pending", FirebaseAuth.getInstance().getUid());

        FirebaseFirestore.getInstance().collection("orders").document(orderNumber).set(orderModel);
        for (int i = 0; i<cartModelList.size();i++ ){
            CartModel cartModel = cartModelList.get(i);
            cartModel.setOrderNumber(orderNumber);

            String id = UUID.randomUUID().toString();
            cartModel.setCartId(id);
            FirebaseFirestore.getInstance().collection("orderproducts").document(id).set(cartModel);
        }

        finish();

    }

    public static int generateRandomNumber(int min, int max) {
        // Ensure that the range is valid
        return (int) (Math.random() * (max - min + 1) + min);
    }
//    public static int randomNumber(int min,int max){
//
//
//        // Create a Random object
//        Random random = new Random();
//
//        // Generate a random number within the specified range
//        return random.nextInt((max - min) + 1) + min;
//    }


}