package com.example.postersmaker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

public class Copybutton {

    public static void onCopy(Context context){
        if (MainActivity.selectedLayer != null) {
            String text =  MainActivity.selectedLayer.getTextView().getText().toString();
            float x = MainActivity.selectedLayer.getFrameLayout().getX();
            float y = MainActivity.selectedLayer.getFrameLayout().getY();
            boolean emoji = MainActivity.selectedLayer.getIsemoji();
            float textsize = MainActivity.selectedLayer.getTextView().getTextSize();
            int width = MainActivity.selectedLayer.getFrameLayout().getWidth();
            int height = MainActivity.selectedLayer.getFrameLayout().getHeight();
            float space = MainActivity.selectedLayer.getTextView().getLetterSpacing();
            float shadow = MainActivity.selectedLayer.getTextView().getShadowRadius();
            int TextviewWidth = MainActivity.selectedLayer.getTextView().getWidth();
            Typeface typeface1 = MainActivity.selectedLayer.getTextView().getTypeface();
            int allignment = MainActivity.selectedLayer.getTextView().getTextAlignment();
            int textColor = MainActivity.selectedLayer.getTextView().getCurrentTextColor();
            int underline = MainActivity.selectedLayer.getTextView().getPaintFlags();
            String hexColor = String.format("#%06X", (0xFFFFFF & textColor));
            boolean flip = MainActivity.selectedLayer.isFlip();
            int font = MainActivity.selectedLayer.getFontResource();

            MainActivity.unselectLayer(MainActivity.selectedLayer);
            MainActivity mainActivity = (MainActivity) context;
            assert mainActivity != null;
            mainActivity.createTextLayout(text, x, y, emoji);
            TextLayout textLayout = MainActivity.combinedItemList.get(MainActivity.combinedItemList.size() - 1).getTextlayout2();
            textLayout.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP,pixelsToSp(textsize,context));
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textLayout.getBorderLayout().getLayoutParams();
            layoutParams.width =width;
            layoutParams.height =  height;
            textLayout.getBorderLayout().setLayoutParams(layoutParams);
            textLayout.getTextView().setTextAlignment(allignment);
            textLayout.getTextView().setWidth(TextviewWidth);
            textLayout.setFontResource(font);
            textLayout.getTextView().setLetterSpacing(space);
            textLayout.getTextView().setShadowLayer(shadow,shadow,shadow, Color.BLACK);
            textLayout.getBorderLayout().setGravity(Gravity.CENTER);
            textLayout.getTextView().setPaintFlags(underline);
            textLayout.setFlip(flip);
            if(flip) {
                textLayout.getTextView().setScaleX(-1f);

            }else {
                textLayout.getTextView().setScaleX(1f);
            }
            textLayout.getTextView().setTextColor(Color.parseColor(hexColor));
            try{
                Typeface typeface = ResourcesCompat.getFont(context, font);
                textLayout.getTextView().setTypeface(typeface);

            }
            catch (Exception e){
                textLayout.getTextView().setTypeface(typeface1);
            }
            MainActivity.selectLayer(textLayout);
        }
        else if(MainActivity.selectedLayer1!=null){
            Uri imageUri = MainActivity.selectedLayer1.getImageUri();
            float x = MainActivity.selectedLayer1.getFrameLayout().getX();
            float y = MainActivity.selectedLayer1.getFrameLayout().getY();
            float currentScaleX = MainActivity.selectedLayer1.getImageView().getScaleX();
            float opacity = MainActivity.selectedLayer1.getImageView().getAlpha();
            ViewGroup.LayoutParams layoutParams1 = MainActivity.selectedLayer1.getImageView().getLayoutParams();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) MainActivity.selectedLayer1.getFrameLayout().getLayoutParams();

            MainActivity.unselectLayers(MainActivity.selectedLayer1);


            MainActivity mainActivity = (MainActivity) context;
            mainActivity.createImageLayout(null, imageUri,null, x, y);
            ImageLayout imageLayout = MainActivity.combinedItemList.get(MainActivity.combinedItemList.size() - 1).getImageLayout();
            imageLayout.getImageView().setLayoutParams(layoutParams1);
            imageLayout.getFrameLayout().setLayoutParams(layoutParams);
            imageLayout.getImageView().setScaleX(currentScaleX);
            imageLayout.getImageView().setAlpha(opacity);
            MainActivity.selectLayers(imageLayout);

        }
        MainActivity.LayerRecycleView.setVisibility(View.GONE);
    }
     private static float pixelsToSp(float px, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }
}
