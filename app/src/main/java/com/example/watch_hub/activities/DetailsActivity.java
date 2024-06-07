package com.example.watch_hub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.watch_hub.R;
import com.example.watch_hub.databinding.ActivityDetailsBinding;
import com.example.watch_hub.domain.CartModel;
import com.example.watch_hub.domain.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;
import java.util.UUID;

public class DetailsActivity extends BaseActivity {
ActivityDetailsBinding binding;
   private ProductModel productModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

         productModel = (ProductModel) intent.getSerializableExtra("model");

        binding.detailsTitle.setText(productModel.getTitle());
        binding.detailsPrice.setText(productModel.getPrice());
        binding.Description.setText(productModel.getDescription());
        binding.ratingTxt.setText(productModel.getRating());

        Glide.with(binding.getRoot()).load(productModel.getImage()).into(binding.detailsProduct);

        binding.backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.addtocartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();


            }
        });

    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(DetailsActivity.this).inflate(R.layout.bottom_layout,(LinearLayout)findViewById(R.id.ainLayout),false);
        bottomSheetDialog.setContentView(view);
        EditText qty = view.findViewById(R.id.bottomQuantity);
        Button btn = view.findViewById(R.id.Continue);
        bottomSheetDialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = qty.getText().toString();
                bottomSheetDialog.cancel();
                addCart(quantity);

            }
        });
    }

    private void addCart(String quan) {
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Adding...");
//        progressDialog.setMessage("Item In Cart");
//        progressDialog.show();
            String id = UUID.randomUUID().toString();
        String orderNumber = String.valueOf(randomNumber(1000,9999));
        CartModel cartModel = new CartModel(null,id,productModel.getTitle(),productModel.getImage(),productModel.getPrice(),quan, FirebaseAuth.getInstance().getUid());
        FirebaseFirestore.getInstance().collection("cart").document(id).set(cartModel);
        Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();

    }
    public static int randomNumber(int min,int max){
        return (new Random()).nextInt((max - min) + 1) + min;
    }

}