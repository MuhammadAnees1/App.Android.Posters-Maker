package com.example.postersmaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JSONFileManager {
    public static void saveCombinedItemsToFile(List<ImageLayout> imageLayouts, List<TextLayout> textLayouts, String filePath) {
        try {
            JSONObject jsonObject = new JSONObject();
            // Convert image layouts to JSON array
            JSONArray imageArray = new JSONArray();
            for (ImageLayout imageLayout : imageLayouts) {
                imageArray.put(convertImageLayoutToJSON(imageLayout));
            }
            jsonObject.put("images", imageArray);

            // Convert text layouts to JSON array
            JSONArray textArray = new JSONArray();
            for (TextLayout textLayout : textLayouts) {
                textArray.put(convertTextLayoutToJSON(textLayout));
            }
            jsonObject.put("texts", textArray);
            // Save the combined JSON to a file
            String jsonString = jsonObject.toString();

            saveStringToFile(jsonString, filePath);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject convertImageLayoutToJSON(ImageLayout imageLayout) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", imageLayout.getId());
            jsonObject.put("imageUri", imageLayout.getImageUri().toString());
            jsonObject.put("x", imageLayout.getFrameLayout().getX());
            jsonObject.put("y", imageLayout.getFrameLayout().getY());
            jsonObject.put("width", imageLayout.getImageView().getWidth());
            jsonObject.put("height", imageLayout.getImageView().getHeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static JSONObject convertTextLayoutToJSON(TextLayout textLayout) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", textLayout.getId());
            jsonObject.put("text", textLayout.getTextView().getText().toString());
            jsonObject.put("x", textLayout.getFrameLayout().getX());
            jsonObject.put("y", textLayout.getFrameLayout().getY());
            jsonObject.put("width", textLayout.getTextView().getWidth());
            jsonObject.put("height", textLayout.getTextView().getHeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    private static void saveStringToFile(String data, String filePath) {
        // Implement your file-saving logic here
        // For example, you can use FileOutputStream or any other method based on your requirements.
    }

}
