package com.example.postersmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FrameImageBackGroundAdapter extends RecyclerView.Adapter<FrameImageBackGroundAdapter.FrameImageViewHolder> {

    private final List<String> frameList;
    private final Context context;
    private final FrameImageClickListener listener;

    public FrameImageBackGroundAdapter(Context context, List<String> backgroundList, FrameImageClickListener listener) {
        this.context = context;
        this.frameList = backgroundList;
        this.listener = listener;
    }

    @Override
    public FrameImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.background_image, parent, false);
        return new FrameImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrameImageViewHolder holder, int position) {
        final String FrameFileName = frameList.get(position);

        // Load the background image using an image-loading library like Glide or Picasso
        // For simplicity, let's assume you have an ImageView in your item_background.xml layout with id "backgroundImageView"
        Glide.with(context)
                .load("file:///android_asset/Basic/" + FrameFileName)
                .into(holder.frameImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the background image filename to the listener
                if(MainActivity.selectedLayer1 != null) {
                    MainActivity.unselectLayers(MainActivity.selectedLayer1);}
                listener.onFrameImageClick(FrameFileName);

            }
        });
    }
    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public static class FrameImageViewHolder extends RecyclerView.ViewHolder {
        ImageView frameImageView;

        public FrameImageViewHolder(View itemView) {
            super(itemView);
            frameImageView = itemView.findViewById(R.id.backgroundImageView);
        }
    }

    public interface FrameImageClickListener {
        void onFrameImageClick(String FrameFileName);
    }
}
