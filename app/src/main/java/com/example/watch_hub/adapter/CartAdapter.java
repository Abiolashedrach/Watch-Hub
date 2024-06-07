package com.example.watch_hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.watch_hub.R;
import com.example.watch_hub.domain.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.myViewHolder> {
    private Context context;
    private final List<CartModel> productModelList;

    public CartAdapter(Context context) {
        this.context = context;
        productModelList = new ArrayList<>();
    }
    public void addProduct(CartModel productModel){
        productModelList.add(productModel);
        notifyDataSetChanged();
    }
    public List<CartModel> getSelectedItems(){
        List<CartModel> cartItems =new  ArrayList<>();
        for (int i = 0;i<productModelList.size();i++){
            cartItems.add(productModelList.get(i));
        }
        return cartItems;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CartModel productModel = productModelList.get(position);
        holder.title.setText(productModel.getProductName());
        holder.price.setText("$"+productModel.getProductPrice());
        holder.qty.setText(productModel.getProductQuantity());
      //  holder.rate.setRating(Float.parseFloat(productModel.getRating()));

        Glide.with(context).load(productModel.getProductImage()).into(holder.img);


       holder.qty.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String QUAN =   holder.qty.getText().toString();
               Map<String,Object> qtyChange = new HashMap<>();
               qtyChange.put("productQuantity",QUAN);
               FirebaseFirestore.getInstance().collection("cart").whereEqualTo("productQuantity",
                       productModel.getProductQuantity()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful() && !task.getResult().isEmpty()){

                           DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                           String documentId = documentSnapshot.getId();
                           FirebaseFirestore.getInstance().collection("cart").document(documentId)
                                   .update(qtyChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {
                                           Toast.makeText(context, "Quantity Changed", Toast.LENGTH_SHORT).show();
                                       }
                                   });

                       }
                   }
               });

           }
       });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
    private TextView title,price;
    private EditText qty;

    private ImageView img;
    private ConstraintLayout constraintLayout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.pName);
            price = itemView.findViewById(R.id.pPrice);
            img = itemView.findViewById(R.id.productpic);
            constraintLayout = itemView.findViewById(R.id.mainLayout);
            qty = itemView.findViewById(R.id.numqty);



        }
    }
}
