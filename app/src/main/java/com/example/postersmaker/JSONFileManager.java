package com.example.postersmaker;

import static com.example.postersmaker.MainActivity.convertPixelsToSP;
import static com.example.postersmaker.MainActivity.imageView;
import static com.example.postersmaker.MainActivity.textLayoutList2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONFileManager {

    public static void saveJSONFile(List<CombinedItem> combinedItemList, Context context) throws JSONException {

        JSONArray jsonArray = new JSONArray();
            JSONObject background = new JSONObject();
            if(MainActivity.imageView.getDrawable() != null)
            {
                Drawable drawable = imageView.getDrawable();

                if (drawable instanceof BitmapDrawable) {
                    // If it's a BitmapDrawable, proceed with Bitmap logic
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    background.put("background", base64Image);
                } else if (drawable instanceof ColorDrawable) {
                    // If it's a ColorDrawable, get the color value
                    int color = ((ColorDrawable) drawable).getColor();

                    background.put("background", String.format("#%06X", (0xFFFFFF & color)));
                }
                String opacity = String.valueOf(imageView.getAlpha());
                Toast.makeText(context, ""+ opacity, Toast.LENGTH_SHORT).show();
                background.put("Opacity",opacity);

            }
            else
        {
            background.put("background", "null");
        }

        jsonArray.put(background);
        for (int i = 0; i < combinedItemList.size(); i++) {
            JSONObject jsonObject = new JSONObject();

            CombinedItem combinedItem = combinedItemList.get(i);
            if (combinedItem.getTextlayout2() != null) {
                jsonObject.put("ComponentName", "text");
                jsonObject.put("Order", i);
                jsonObject.put("ID", combinedItem.getTextlayout2().getId());
                jsonObject.put("emoji", combinedItem.getTextlayout2().isemoji);

                jsonObject.put("Text ID",TextHandlerClass.textLayoutList.indexOf(combinedItem.getTextlayout2().getFrameLayout()));
                jsonObject.put("text",combinedItem.getTextlayout2().getTextView().getText());
                jsonObject.put("PositionX",combinedItem.getTextlayout2().getFrameLayout().getX());
                jsonObject.put("TextSize", combinedItem.getTextlayout2().getTextView().getTextSize());
                jsonObject.put("ViewGroup", combinedItem.getTextlayout2().getFrameLayout().getParent());
                jsonObject.put("PositionY",combinedItem.getTextlayout2().getFrameLayout().getY());

                if(combinedItem.getTextlayout2().getFontResource() != 0) {
                    jsonObject.put("Font", combinedItem.getTextlayout2().getFontResource());
                }
                else {
                    jsonObject.put("Font", 0);
                }
                Typeface typeface = combinedItem.getTextlayout2().getTextView().getTypeface();
                if(typeface != null) {

                if ( typeface.getStyle() == Typeface.BOLD) {
                    jsonObject.put("typeface", "bold");
                }
                else if(typeface.getStyle() == Typeface.ITALIC) {
                    jsonObject.put("typeface", "italic");
                }
                else if (typeface.getStyle() == Typeface.BOLD_ITALIC) {
                    jsonObject.put("typeface", "bold italic");
                }
                else {
                    jsonObject.put("typeface", "normal");
                }}

                int textColor = combinedItem.getTextlayout2().getTextView().getCurrentTextColor();
                String hexColor = String.format("#%06X", (0xFFFFFF & textColor));
                jsonObject.put("Color", hexColor);
                jsonObject.put("Width",combinedItem.getTextlayout2().getBorderLayout().getWidth());
                jsonObject.put("Height",combinedItem.getTextlayout2().getBorderLayout().getHeight());
                jsonObject.put("TextAlignment",combinedItem.getTextlayout2().getTextView().getTextAlignment());
                jsonObject.put("Alpha",combinedItem.getTextlayout2().getTextView().getAlpha());


                if(combinedItem.getTextlayout2().getTextView().getShadowRadius() != 0) {
                    jsonObject.put("Shadow Radius", combinedItem.getTextlayout2().getTextView().getShadowRadius());
                    jsonObject.put("Shadow dx", combinedItem.getTextlayout2().getTextView().getShadowDx());
                    jsonObject.put("Shadow dy", combinedItem.getTextlayout2().getTextView().getShadowDy());
                }
                else {
                    jsonObject.put("Shadow Radius", 0);
                    jsonObject.put("Shadow dx", 0);
                    jsonObject.put("Shadow dy", 0);
                }

                jsonObject.put("Rotation",combinedItem.getTextlayout2().getFrameLayout().getRotation());
                jsonObject.put("spacing",combinedItem.getTextlayout2().getTextView().getLetterSpacing());
                jsonObject.put("Lock",combinedItem.getTextlayout2().getLocked());


            } else if (combinedItem.getImageLayout() != null) {
                jsonObject.put("ComponentName", "image");
                jsonObject.put("Order", i);
                jsonObject.put("image",combinedItem.getImageLayout().getImageUri());
                jsonObject.put("PositionX",combinedItem.getImageLayout().getFrameLayout().getX());
                jsonObject.put("PositionY",combinedItem.getImageLayout().getFrameLayout().getY());
                jsonObject.put("Width",combinedItem.getImageLayout().getImageView().getWidth());
                jsonObject.put("Height",combinedItem.getImageLayout().getImageView().getHeight());
                jsonObject.put("Alpha",combinedItem.getImageLayout().getImageView().getAlpha());
                jsonObject.put("Lock",combinedItem.getImageLayout().getLocked());
                jsonObject.put("Rotation",combinedItem.getImageLayout().getFrameLayout().getRotation());

            }
            jsonArray.put(jsonObject);
            }
            File appDirectory = new File(context.getExternalFilesDir(null), "Postermaker");
            if (!appDirectory.exists()) {
                appDirectory.mkdirs();
            }
            File jsonFile = new File(appDirectory, "your_file.json");
            try (FileOutputStream fileOutputStream = new FileOutputStream(jsonFile)) {
                fileOutputStream.write(jsonArray.toString().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }
}
