package com.example.postersmaker;

import static com.example.postersmaker.MainActivity.callSetDefaultState;
import static com.example.postersmaker.MainActivity.container;
import static com.example.postersmaker.MainActivity.imageView;
import static com.example.postersmaker.MainActivity.parentLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.FragmentTransaction;
import com.github.dhaval2404.imagepicker.ImagePicker;
import java.util.ArrayList;
import java.util.List;

public class ImagePickerManager {
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
            }



        }
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("openedFromImagePickerManager", true);
            homeFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container3, homeFragment);
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
            ImageLayout imageLayout = mainActivity.createImageLayout(null,imageUri, null,x, y);
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
                    viewGroup.addView(frameLayout);
                    animateViewEvolveFromCenter(imageLayout.getImageView(), 1000);



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
    public static void animateViewEvolveFromCenter(ImageView imageView, long duration) {
        // Set initial properties
        imageView.setAlpha(0f);
        imageView.setScaleX(0f);
        imageView.setScaleY(0f);

        // Create the ObjectAnimator for translation
        ObjectAnimator translationX = ObjectAnimator.ofFloat(imageView, "translationX", 0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(imageView, "translationY", 0f);

        // Create the ObjectAnimator for alpha
        ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView, "alpha", 1f);

        // Create the ObjectAnimator for scale
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f);

        // Combine all animations into an AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, translationY, alpha, scaleX, scaleY);
        animatorSet.setDuration(duration);

        // Start the animation
        animatorSet.start();
    }
}
