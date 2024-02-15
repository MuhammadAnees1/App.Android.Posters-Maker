package com.example.postersmaker;

import android.widget.ImageView;

public class HueItem {
    private float hueValue;
    private ImageView image;

    public ImageView getImage() {
        return image;
    }

    public HueItem(float hueValue , ImageView image) {
        this.hueValue = hueValue;
        this.image = image;
    }

    public float getHueValue() {
        return hueValue;
    }
}
