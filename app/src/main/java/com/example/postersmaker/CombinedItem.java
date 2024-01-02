package com.example.postersmaker;

public class CombinedItem {
    private String text;
    private ImageLayout imageLayout;

    public CombinedItem(String text) {
        this.text = text;
        this.imageLayout = null; // Set this to the corresponding ImageLayout if it's an image item
    }

    public CombinedItem(ImageLayout imageLayout) {
        this.text = null; // Set this to the corresponding text if it's a text item
        this.imageLayout = imageLayout;
    }


    public String getText() {
        return text;
    }

    public ImageLayout getImageLayout() {
        return imageLayout;
    }
}

