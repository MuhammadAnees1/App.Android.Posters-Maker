package com.example.postersmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BackGroundEffectsAdapter extends RecyclerView.Adapter<BackGroundEffectsAdapter.BackgroundImageViewHolder> {

    private final List<String> EffectList;
    private final Context context;
    private final BackgroundEffectClickListener listener;

    public BackGroundEffectsAdapter(Context context, List<String> backgroundList, BackgroundEffectClickListener listener) {
        this.context = context;
        this.EffectList = backgroundList;
        this.listener = listener;
    }

    @Override
    public BackgroundImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.background_image, parent, false);
        return new BackgroundImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BackgroundImageViewHolder holder, int position) {
        final String backgroundFileName = EffectList.get(position);

        // Load the background image using an image-loading library like Glide or Picasso
        // For simplicity, let's assume you have an ImageView in your item_background.xml layout with id "backgroundImageView"
        Glide.with(context)
                .load("file:///android_asset/effect/" + backgroundFileName)
                .into(holder.backgroundImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the background image filename to the listener
                listener.onBackgroundEffectClick(backgroundFileName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return EffectList.size();
    }

    public static class BackgroundImageViewHolder extends RecyclerView.ViewHolder {
        ImageView backgroundImageView;

        public BackgroundImageViewHolder(View itemView) {
            super(itemView);
            backgroundImageView = itemView.findViewById(R.id.backgroundImageView);
        }
    }

    public interface BackgroundEffectClickListener {
        void onBackgroundEffectClick(String backgroundFileName);
    }
}
