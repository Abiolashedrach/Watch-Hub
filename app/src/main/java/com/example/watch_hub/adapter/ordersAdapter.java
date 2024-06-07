package com.example.watch_hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watch_hub.R;
import com.example.watch_hub.domain.CartModel;
import com.example.watch_hub.domain.OrderModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ordersAdapter extends RecyclerView.Adapter<ordersAdapter.myViewHolder> {
    private Context context;
    private final List<OrderModel> productModelList;

    public ordersAdapter(Context context) {
        this.context = context;
        productModelList = new ArrayList<>();
    }
    public void addProduct(OrderModel productModel){
        productModelList.add(productModel);
        notifyDataSetChanged();
    }
    public List<OrderModel> getSelectedItems(){
        List<OrderModel> cartItems =new  ArrayList<>();
        for (int i = 0;i<productModelList.size();i++){
            cartItems.add(productModelList.get(i));
        }
        return cartItems;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order, parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
       OrderModel orderModel = productModelList.get(position);
        holder.title.setText(orderModel.getCustomerName());
        holder.orderNum.setText(orderModel.getOrderNumber());
        int cod = Integer.parseInt(orderModel.getItemExpense());
        holder.price.setText(String.valueOf(cod));
        holder.status.setText(orderModel.getOrderStatus());

        CartAdapter cartAdapter = new CartAdapter(context);
        holder.products.setAdapter(cartAdapter);
        holder.products.setLayoutManager(new LinearLayoutManager(context));

        FirebaseFirestore.getInstance().collection("orderProducts").whereEqualTo("orderNumber",orderModel.getOrderNumber())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            CartModel cartModel = ds.toObject(CartModel.class);
                            cartAdapter.addProduct(cartModel);
                        }
                    }
                });



    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
    private TextView title,orderNum,status,price ;
        private RecyclerView products;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.pName);
            orderNum = itemView.findViewById(R.id.orderNum);
            status = itemView.findViewById(R.id.status);
            price = itemView.findViewById(R.id.orderPrice);
            products = itemView.findViewById(R.id.productsRecycler);



        }
    }
}
