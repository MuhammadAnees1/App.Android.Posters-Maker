package com.example.postersmaker;


import static com.example.postersmaker.MainActivity.convertPixelsToSP;
import static com.example.postersmaker.MainActivity.drawPaintView;
import static com.example.postersmaker.MainActivity.filterView;
import static com.example.postersmaker.MainActivity.imageView;
import static com.example.postersmaker.MainActivity.parentLayout;
import static com.example.postersmaker.MainActivity.selectedLayer;
import static com.example.postersmaker.MainActivity.selectedLayer1;
import static com.example.postersmaker.MainActivity.textLayoutList2;
import static com.example.postersmaker.MainActivity.unselectLayer;
import static com.example.postersmaker.MainActivity.unselectLayers;
import static com.example.postersmaker.TextHandlerClass.textLayoutList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONReader {
    static FrameLayout thisFrameLayout;
    public static void readJSONFile(Context context) {

        File jsonFile = new File(context.getExternalFilesDir(null), "Postermaker/your_file.json");

        if (!jsonFile.exists()) {
            Toast.makeText(context, "File does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(jsonFile)) {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Parse the JSON data
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());

            // Process each item in the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.has("background")) {
                    try {
                        String base64Image = jsonObject.getString("background");
                        Drawable backgroundDrawable;

                        // Check if the base64Image represents a color or an image
                        if (base64Image.startsWith("#")) {
                            // It's a color, create a ColorDrawable
                            int color = Color.parseColor(base64Image);
                            backgroundDrawable = new ColorDrawable(color);
                        } else {
                            // It's an image, decode the Base64 string to a Bitmap
                            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                            backgroundDrawable = new BitmapDrawable(context.getResources(), decodedBitmap);
                        }
                        imageView.setImageDrawable(backgroundDrawable);
                        String opacityString = jsonObject.getString("Opacity");
                        float opacity = Float.parseFloat(opacityString);
                        Toast.makeText(context, ""+opacity+opacityString, Toast.LENGTH_SHORT).show();
                        imageView.setAlpha(opacity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//
                 else {
                    // Handle other items (text or image)
                    String componentName = jsonObject.getString("ComponentName");
                    int order = jsonObject.getInt("Order");

                    if (componentName.equals("text")) {
                        String text = jsonObject.getString("text");
                        float positionX = (float) jsonObject.getDouble("PositionX");
                        float positionY = (float) jsonObject.getDouble("PositionY");
                        int width = jsonObject.getInt("Width");
                        int height = jsonObject.getInt("Height");
                        boolean emoji = jsonObject.getBoolean("emoji");
                        int textAlignment = jsonObject.getInt("TextAlignment");
                        float textSize = convertPixelsToSP(jsonObject.getInt("TextSize"), context);
                        float alpha = jsonObject.getInt("Alpha");
                        float shadow = jsonObject.getInt("Shadow Radius");
                        float rotation = jsonObject.getInt("Rotation");
                        float spacing = jsonObject.getInt("spacing");
                        boolean lock = jsonObject.getBoolean("Lock");
                        int textID = jsonObject.getInt("Text ID");
                        float shadowDx = (float) jsonObject.getDouble("Shadow dx");
                        float shadowDy = (float) jsonObject.getDouble("Shadow dy");
                        int font = jsonObject.getInt("Font");
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.createTextLayout(text, positionX, positionY, emoji);

                        TextLayout textLayout = MainActivity.combinedItemList.get(order).getTextlayout2();
                        textLayout.setLocked(lock);
                        textLayout.getFrameLayout().setRotation(rotation);


                        textLayout.getBorderLayout().getLayoutParams().width = width;
                        textLayout.getBorderLayout().getLayoutParams().height = height;

                        TextView textView1 = new TextView(context);
                        textView1.setText(text);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                        textLayout.setMaxSize(textView1.getTextSize());
                        String hexColor = jsonObject.getString("Color");
                        int textColor = Color.parseColor(hexColor);
                        textView1.setTextColor(textColor);
                        textView1.setTextAlignment(textAlignment);
                        if(font != 0) {
                            Typeface typeface = ResourcesCompat.getFont(context, font);
                            textView1.setTypeface(typeface);
                            textLayout.setFontResource(font);
                        }
                        else{
                        String typeface1 = jsonObject.getString("typeface");
                        if(typeface1 != "null") {
                            if(typeface1.equals("bold")) {
                                textView1.setTypeface(null,Typeface.BOLD);
                            }
                          else if(typeface1.equals("italic")) {
                                textView1.setTypeface(null,Typeface.ITALIC);
                            }
                            else if(typeface1.equals("bold italic")) {
                                textView1.setTypeface(null,Typeface.BOLD_ITALIC);
                            }
                            else  {
                                textView1.setTypeface(null,Typeface.NORMAL);
                            }

                        }}
                        if(alpha != 0) {
                            textView1.setAlpha(alpha);
                        }
                        if(spacing != 0) {
                            textView1.setLetterSpacing(spacing);
                        }
                        if(shadow != 0) {
                            textView1.setShadowLayer(shadow, shadowDx, shadowDy, Color.BLACK);
                        }
                        TextHandlerClass.textList.add(text);



                        textLayout.getBorderLayout().removeAllViews();
                        textLayout.setTextView(textView1);
                        textLayout.getBorderLayout().addView(textLayout.getTextView());
                        textLayoutList2.add(textLayout);
                        textLayoutList.add(textLayout.getFrameLayout());


                        textLayout.getFrameLayout().setRotation(rotation);

                        textLayout.getBorderLayout().requestLayout();
                        textLayout.getBorderLayout().invalidate();
                        textLayout.getFrameLayout().requestLayout();
                        textLayout.getBorderLayout().requestLayout();
                        textLayout.getTextView().invalidate();

                        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                        HomeFragment1 homeFragment1 = new HomeFragment1();
                        fragmentTransaction.replace(R.id.fragment_container, homeFragment1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        mainActivity.defaultContainer();
                        if(selectedLayer == textLayout){
                            unselectLayer(textLayout);
                        }

                        }

                     else if (componentName.equals("image")) {
                    // Handle image information
                    String imageUri = jsonObject.getString("image");
                    float positionX = (float) jsonObject.getDouble("PositionX");
                    float positionY = (float) jsonObject.getDouble("PositionY");
                    float rotation = jsonObject.getInt("Rotation");
                    int height = jsonObject.getInt("Height");
                    int width = jsonObject.getInt("Width");
                    float alpha = (float) jsonObject.getDouble("Alpha");
                    boolean lock = jsonObject.getBoolean("Lock");

                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.createImageLayout(null,Uri.parse(imageUri), null, positionX, positionY);

                   ImageLayout imageLayout =  MainActivity.combinedItemList.get(order).getImageLayout();
                   imageLayout.getFrameLayout().setRotation(rotation);
                   imageLayout.getImageView().getLayoutParams().width = width;
                   imageLayout.getImageView().getLayoutParams().height = height;
                   ImagePickerManager.imageLayoutList.add(imageLayout);
                   imageLayout.setImageUri(Uri.parse(imageUri));
                   imageLayout.setLocked(lock);
                   imageLayout.getFrameLayout().setRotation(rotation);
                   imageLayout.getImageView().setAlpha(alpha);

                        if(selectedLayer1 == imageLayout){
                            unselectLayers(imageLayout);
                        }
                        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                        HomeFragment1 homeFragment1 = new HomeFragment1();
                        fragmentTransaction.replace(R.id.fragment_container, homeFragment1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                }


                }


            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }
    static TextLayout findtextLayout(FrameLayout frameLayout){
        for (TextLayout textLayout : MainActivity.textLayoutList2) {
            if(textLayout.getFrameLayout() == frameLayout)
            {
                break;
            }
            return textLayout;
            }
        return null;
    }



}
