package com.example.postersmaker;

import java.util.ArrayList;
import java.util.List;

public class CombinedItem {
    private TextLayout textlayout2;
    private ImageLayout imageLayout;
    public static List<Integer> ids = new ArrayList<>();

    public CombinedItem(TextLayout textlayout2) {
        this.textlayout2 = textlayout2;
        this.imageLayout = null; // Set this to the corresponding ImageLayout if it's an image item
    }

    public CombinedItem(ImageLayout imageLayout) {
        this.textlayout2 = null; // Set this to the corresponding text if it's a text item
        this.imageLayout = imageLayout;
    }


    public TextLayout getTextlayout2() {
        return textlayout2;
    }

    public ImageLayout getImageLayout() {
        return imageLayout;
    }


}
