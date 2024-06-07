package com.example.watch_hub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.watch_hub.R;
import com.example.watch_hub.activities.DetailsActivity;
import com.example.watch_hub.domain.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.myViewHolder> {
    private Context context;
    private List<ProductModel> productModelList;

    public StoreAdapter(Context context) {
        this.context = context;
        productModelList = new ArrayList<>();
    }
    public void addProduct(ProductModel productModel){
        productModelList.add(productModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop, parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        ProductModel productModel = productModelList.get(position);
        holder.title.setText(productModel.getTitle());
        holder.price.setText("$"+productModel.getPrice());
        holder.ratingCount.setText("("+productModel.getRating()+")");
      //  holder.rate.setRating(Float.parseFloat(productModel.getRating()));

        Glide.with(context).load(productModel.getImage()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("model",productModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public void searchDataList(ArrayList<ProductModel> searchList){
        productModelList = searchList;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
    private TextView title,ratingCount,price,description,reviewCount;

    private RatingBar rate;
    private ImageView img;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tittle);
            ratingCount = itemView.findViewById(R.id.ratingTxt);
            price = itemView.findViewById(R.id.newprice);
            rate = itemView.findViewById(R.id.ratingBar);
            img = itemView.findViewById(R.id.productpic);
            reviewCount = itemView.findViewById(R.id.reviewcount);

        }
    }
}
