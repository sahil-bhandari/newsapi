package com.sahil.daggerdemo.HomePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sahil.daggerdemo.R;
import com.sahil.daggerdemo.WebViewActivity;
import com.sahil.daggerdemo.models.Article;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private final OnItemClickListener listener;
    private List<Article> data;
    private Context context;

    public HomeAdapter(Context context, List<Article> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.click(data.get(position), listener);
        holder.tvTitle.setText(data.get(position).getTitle());
        holder.tvDesc.setText(data.get(position).getDescription());

        String images = data.get(position).getUrlToImage();

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(context)
                .load(images)
                .apply(requestOptions)
                .into(holder.background);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onClick(Article Item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDesc;
        ImageView background;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            tvDesc = itemView.findViewById(R.id.content);
            background = itemView.findViewById(R.id.image);
        }


        public void click(final Article ListData, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(ListData);
                }
            });
        }
    }
}
