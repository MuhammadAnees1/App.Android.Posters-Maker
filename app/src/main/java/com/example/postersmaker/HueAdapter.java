package com.example.postersmaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.ViewUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

// Import statements...

public class HueAdapter extends RecyclerView.Adapter<HueAdapter.HueViewHolder> {
    private List<HueItem> hueItemList;
    ImageView imageView5;
    public interface OnHueItemClickListener {
        void onHueItemClick(float hueValue);
    }

    public HueAdapter(List<HueItem> hueItemList) {
        this.hueItemList = hueItemList;
    }

    @NonNull
    @Override
    public HueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hue_image, parent, false);
         imageView5 = view.findViewById(R.id.hueImageView);
        return new HueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HueViewHolder holder, int position) {
        HueItem hueItem = hueItemList.get(position);

        holder.bind(hueItem);
    }

    @Override
    public int getItemCount() {
        return hueItemList.size();
    }

    public class HueViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public HueViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hueImageView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HueItem hueItem = hueItemList.get(position);
                    applyHueToImageView(MainActivity.selectedLayer1.getImageView().getDrawable(), hueItem.getHueValue());
                }
            });
        }

        public void bind(HueItem hueItem) {
            Bitmap bitmap = getBitmapFromView(hueItem.getImage());
            imageView5.setImageBitmap(bitmap);
            applyHueToImageView(imageView5.getDrawable(), hueItem.getHueValue());

        }
    }

    private void applyHueToImageView(Drawable originalDrawable, float hue) {
        Drawable modifiedDrawable = originalDrawable;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setRotate(0, hue);
        colorMatrix.setRotate(1, hue);
        colorMatrix.setRotate(2, hue);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

        modifiedDrawable.setColorFilter(colorFilter);
    }
    public Bitmap getBitmapFromView(ImageView view) {
        // Check if the view has been laid out
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            return null;
        }

        // Create a Bitmap with the same dimensions as the view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a Canvas using the Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Draw the view's visible content onto the Canvas
        view.draw(canvas);

        return bitmap;
    }
}
