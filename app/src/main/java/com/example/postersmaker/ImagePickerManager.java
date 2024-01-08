package com.example.postersmaker;

import static com.example.postersmaker.MainActivity.container;
import static com.example.postersmaker.MainActivity.textLayoutList2;
import static com.example.postersmaker.TextHandlerClass.convertTextLayoutToJSON;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.FragmentTransaction;
import com.github.dhaval2404.imagepicker.ImagePicker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ImagePickerManager {
    public static JSONObject convertImageLayoutToJSON(ImageLayout imageLayout, int order) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", imageLayout.getId());
            jsonObject.put("order", order);
            jsonObject.put("imageUri", imageLayout.getImageUri().toString());
            jsonObject.put("positionX", imageLayout.getFrameLayout().getX());
            jsonObject.put("positionY", imageLayout.getFrameLayout().getY());
            jsonObject.put("width", imageLayout.getImageView().getWidth());
            jsonObject.put("height", imageLayout.getImageView().getHeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONArray convertCombinedLayoutListToJSON() {
        JSONArray jsonArray = new JSONArray();
        int order = 1;
        for (ImageLayout imageLayout : imageLayoutList) {
            jsonArray.put(convertImageLayoutToJSON(imageLayout, order++));
        }
        for (TextLayout textLayout : textLayoutList2) {
            jsonArray.put(convertTextLayoutToJSON(textLayout, order++));
        }
        return jsonArray;
    }
    private static final int IMAGE_PICK_REQUEST = 100;
    private static final String TAG = "ImagePickerManager";
    static List<ImageLayout> imageLayoutList = new ArrayList<>();
    public static void openGallery(Activity activity) {
        ImagePicker.Companion
                .with(activity)
                .galleryOnly()
                .crop()
                .compress(2048)
                .maxResultSize(500, 500)
                .start(IMAGE_PICK_REQUEST);
    }
    public static void handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            Log.d(TAG, "handleActivityResult: Image URI - " + imageUri);
            // Proceed with your logic
            if (imageUri != null) {
                addImageToContainer(activity, activity.findViewById(android.R.id.content), imageUri, 40, 40);
                JSONArray jsonArray = convertCombinedLayoutListToJSON();
                Log.d(TAG, "convertCombinedLayoutListToJSON: JSON - " + jsonArray);

            }

        }
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("openedFromImagePickerManager", true);
            homeFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, homeFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    // Updated method to add the selected image to the container
    public static void addImageToContainer(Context context, ViewGroup viewGroup, Uri imageUri, float x, float y) {
        if (viewGroup == null) {
            Log.e(TAG, "addImageToContainer: ViewGroup is null");
            return;
        }

        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;

            // Check if there's an existing selected layer and unselect it
            if (MainActivity.selectedLayer1 != null) {
                MainActivity.unselectLayers(MainActivity.selectedLayer1);
            }
            // Create a new ImageLayout based on the provided imageUri
            ImageLayout imageLayout = mainActivity.createImageLayout(imageUri, null,x, y);
            imageLayout.setImageUri(imageUri);
            FrameLayout frameLayout = imageLayout.getFrameLayout();
            if (imageUri == null) {
                mainActivity.defaultContainer();
            }

            if (frameLayout != null) {
                // Set the position of the frameLayout based on x and y
                frameLayout.setX(x);
                frameLayout.setY(y);
                container.setVisibility(View.VISIBLE);

                // Check if the frameLayout already has a parent
                if (frameLayout.getParent() == null) {
                    // Add the frameLayout to the viewGroup
                    viewGroup.addView(frameLayout);

                    // Add an action for undo and redo
                    mainActivity.addAction(new MainActivity.CustomAction(
                            () -> {
                                // Undo logic: remove the frameLayout from the viewGroup
                                viewGroup.removeView(frameLayout);
                                imageLayoutList.remove(imageLayout);
                            },
                            () -> {
                                // Redo logic: add the frameLayout back to the viewGroup
                                viewGroup.addView(frameLayout);
                                imageLayoutList.add(imageLayout);
                            }
                    ));
                } else {
                    Log.e(TAG, "addImageToContainer: FrameLayout already has a parent");
                }
                // Add the frameLayout to the list
                imageLayoutList.add(imageLayout);
                Log.d(TAG, "imageLayoutList size: " + imageLayoutList.size());


            } else {
                Log.e(TAG, "addImageToContainer: FrameLayout is null");
            }
        } else {
            Log.e(TAG, "addImageToContainer: Invalid context");
        }
    }
}
