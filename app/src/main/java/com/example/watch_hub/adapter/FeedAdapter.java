package com.example.watch_hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.watch_hub.R;
import com.example.watch_hub.domain.FeedModel;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
    private final Context context;
    private final List<FeedModel> feedList;

    public FeedAdapter(Context context) {
        this.context = context;
        this.feedList = new ArrayList<>();
    }

    public void addFeedList(List<FeedModel> feedList) {
        this.feedList.addAll(feedList);
        notifyDataSetChanged();
    }

    public void clear() {
        feedList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_feed, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FeedModel feedModel = feedList.get(position);
        Glide.with(context).load(feedModel.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.feedpic);
        }
    }
}
