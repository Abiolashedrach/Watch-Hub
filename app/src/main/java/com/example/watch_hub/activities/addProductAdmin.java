package com.example.watch_hub.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_hub.databinding.ActivityAddProductAdminBinding;
import com.example.watch_hub.domain.ProductModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addProductAdmin extends AppCompatActivity {
ActivityAddProductAdminBinding binding;

private String Name, price, description, rating,categories;

private Uri uri;



@Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddProductAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    String[] items = new String[]{"Smart Watches", "Casual Watches", "High-End Watches", "Luxrious Watches", "Ladies Watches"};

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

        binding.categoryTxt.setAdapter(adapter);

        binding.categoryTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(addProductAdmin.this,"item "+item, Toast.LENGTH_SHORT).show();
            }
        });

        binding.UploadProductBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Name = binding.brandName.getText().toString();
            price = binding.productPrice.getText().toString();
            description = binding.productDescription.getText().toString();
            rating = binding.productRating.getText().toString();
            categories = binding.categoryTxt.getText().toString();

            addProduct();
                uploadImage();
                finish();
            }
        });
        binding.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });

    }

    private void uploadImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("products/"+Name+".png");
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      FirebaseFirestore.getInstance().collection("products").document(Name).update("image",uri.toString());
                        Toast.makeText(addProductAdmin.this, "Done Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
           uri = data.getData();
            binding.productImage.setImageURI(uri);
        }
    }
    private void addProduct() {
        ProductModel productModel = new ProductModel(Name,price,rating,description,null,true,categories);

        FirebaseFirestore.getInstance().collection("products").document(Name).set(productModel);

        Toast.makeText(this, "Added Succesfully", Toast.LENGTH_SHORT).show();

    }
}