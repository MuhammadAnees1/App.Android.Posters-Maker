package com.example.postersmaker;

import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            background.put("background", MainActivity.imageView);
            background.put("Current Background Drawable" , MainActivity.CurrentImg);
            background.put("Blurr", MainActivity.originalBitmap1);
        for (int i = 0; i < combinedItemList.size(); i++) {
            JSONObject jsonObject = new JSONObject();

            CombinedItem combinedItem = combinedItemList.get(i);
            if (combinedItem.getTextlayout2() != null) {
                jsonObject.put("ComponentName", "text");
                jsonObject.put("Order", i);
                jsonObject.put("text",combinedItem.getTextlayout2().getTextView().getText());
                jsonObject.put("PositionX",combinedItem.getTextlayout2().getFrameLayout().getX());
                jsonObject.put("PositionY",combinedItem.getTextlayout2().getFrameLayout().getY());
                jsonObject.put("Font",combinedItem.getTextlayout2().getTextView().getTypeface());
                jsonObject.put("Color",combinedItem.getTextlayout2().getTextView().getTextColors());
                jsonObject.put("Width",combinedItem.getTextlayout2().getTextView().getWidth());
                jsonObject.put("Height",combinedItem.getTextlayout2().getTextView().getHeight());
                jsonObject.put("TextAlignment",combinedItem.getTextlayout2().getTextView().getTextAlignment());
                jsonObject.put("TextSize",combinedItem.getTextlayout2().getTextView().getTextSize());
                jsonObject.put("Alpha",combinedItem.getTextlayout2().getTextView().getAlpha());
                jsonObject.put("Shadow",combinedItem.getTextlayout2().getTextView().getShadowRadius());
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
