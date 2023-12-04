package com.example.postersmaker;// ColorPlateAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ColorPlateAdapter extends RecyclerView.Adapter<ColorPlateAdapter.ColorViewHolder> {

    private Context context;
    private List<Integer> colorResourceIds;

    private OnColorClickListener onColorClickListener;

    public ColorPlateAdapter(Context context, List<Integer> colorResourceIds, OnColorClickListener listener) {
        this.context = context;
        this.colorResourceIds = colorResourceIds;
        this.onColorClickListener = listener;
    }
    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_plate_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int colorResourceId = colorResourceIds.get(position);
        int color = ContextCompat.getColor(context, colorResourceId);
        holder.bind(color);
    }

    @Override
    public int getItemCount() {
        return colorResourceIds.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        ImageView colorPlateImageView;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorPlateImageView = itemView.findViewById(R.id.colorPlateImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onColorClickListener != null) {
                        onColorClickListener.onColorClick(colorResourceIds.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void bind(int color) {
            colorPlateImageView.setBackgroundColor(color);
        }
    }

    public interface OnColorClickListener {
        void onColorClick(int color);
    }
}
