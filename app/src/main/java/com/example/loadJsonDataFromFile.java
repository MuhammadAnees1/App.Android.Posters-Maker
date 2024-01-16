package com.example;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.example.postersmaker.HomeFragment;
import com.example.postersmaker.MainActivity;
import com.example.postersmaker.R;
import com.example.postersmaker.TextLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class loadJsonDataFromFile {
    private final Context context;
    public loadJsonDataFromFile(Context context) {
        this.context = context;
    }
    public void loadJsonData() {
        try {
            MainActivity mainActivity = (MainActivity) context;
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("data.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            bufferedReader.close();
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String componentName = jsonObject.getString("ComponentName");
                Log.d("TAG", "Json object: " + jsonObject);
                Log.d("TAG", "Component name: " + componentName);

                if ("imageView".equals(componentName)) {
                    String imageUrl = jsonObject.getString("image");
                    float positionX = (float) jsonObject.getDouble("PositionX");
                    float positionY = (float) jsonObject.getDouble("PositionY");

                    Uri imageUri = Uri.parse(imageUrl);
                    // Create and add ImageLayout to your layout
                    mainActivity.createImageLayout(imageUri, null, positionX, positionY);
                    Log.d("TAG", "ImageView created: " + imageUri.toString());
                    FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.fragment_container, homeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("textView".equals(componentName)) {
                    String text = jsonObject.getString("text");
                    float positionX = (float) jsonObject.getDouble("PositionX");
                    float positionY = (float) jsonObject.getDouble("PositionY");
                    // Create and add TextLayout to your layout
                    mainActivity.createTextLayout(text, positionX, positionY);
                    Log.d("TAG", "TextLayout created: " + text);
                }
            }
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
