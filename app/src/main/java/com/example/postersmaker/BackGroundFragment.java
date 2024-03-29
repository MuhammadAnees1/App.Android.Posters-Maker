package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.postersmaker.HomeFragment.getYourColorList;
import static com.example.postersmaker.MainActivity.CurrentImg;
import static com.example.postersmaker.MainActivity.imageUri1;
import static com.example.postersmaker.MainActivity.imageView;
import static com.example.postersmaker.MainActivity.originalBitmap1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BackGroundFragment extends Fragment implements MainImageBackGroundAdapter.BackgroundImageClickListener {
    MainImageBackGroundAdapter mainImageBackGroundAdapter;

    RecyclerView recyclerView;
    private static final int IMAGE_PICK_REQUEST = 101;

    static float lastProgress = 0;
    private BlurProcessor blurProcessor;

    SeekBar blurSeekBar, OpacityBackgroundSeekbar;
    ImageView PickBackGroundButton, PickBackGroundColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_back_ground, container, false);

        recyclerView = view.findViewById(R.id.SetBackGroundLayout);
        PickBackGroundButton = view.findViewById(R.id.PickBackGroundButton);
        PickBackGroundColor = view.findViewById(R.id.PickBackGroundColor);
        blurSeekBar = view.findViewById(R.id.blurSeekBar);
        OpacityBackgroundSeekbar = view.findViewById(R.id.OpacityBackground);
        blurProcessor = new BlurProcessor(requireActivity());
        if (MainActivity.container.getVisibility() == View.GONE || MainActivity.container.getVisibility() == View.INVISIBLE) {
            MainActivity.container.setVisibility(View.VISIBLE);
//            MainActivity.container.startAnimation(MainActivity.fadeIn);
        }
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        mainImageBackGroundAdapter = new MainImageBackGroundAdapter(getActivity(), getBackgroundList(getContext()), this);
        recyclerView.setAdapter(mainImageBackGroundAdapter);

        PickBackGroundColor.setOnClickListener(v -> showColorPickerDialog());

        blurSeekBar.setOnSeekBarChangeListener(onSeekBarChanged());
        blurSeekBar.setProgress((int) lastProgress);

        OpacityBackgroundSeekbar.setProgress(100);
        OpacityBackgroundSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Reverse the progress by subtracting the current progress from the maximum progress

                MainActivity mainActivity = (MainActivity) getActivity();
                assert mainActivity != null;
                mainActivity.OpacityBackground(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        PickBackGroundButton.setOnClickListener(v -> {
            // Launch ImagePicker to choose an image from the gallery
            ImagePicker.Companion
                    .with(requireActivity())
                    .crop()
                    .compress(2048)
                    .maxResultSize(1080, 1080)
                    .start(IMAGE_PICK_REQUEST);
        });

        return view;
    }

    private List<String> getBackgroundList(Context context) {
        List<String> backGroundList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            // List all files in the "Cover" directory inside the "assets" folder
            String[] backgroundFiles = assetManager.list("Cover");
            if (backgroundFiles != null) {
                // Assuming your background files have a consistent naming convention
                Arrays.sort(backgroundFiles);
                backGroundList.addAll(Arrays.asList(backgroundFiles));
                // Print the list of loaded background files for debugging
                for (String fileName : backGroundList) {
                    Log.d("BackgroundFragment", "Loaded background file: " + fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("BackgroundFragment", "Error loading background images: " + e.getMessage());
        }
        return backGroundList;
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: data" + IMAGE_PICK_REQUEST);
            Uri imageUri = data.getData();
            MainActivity mainActivity = (MainActivity) getActivity();
            // Update the background image in the MainActivity
            if (mainActivity != null) {
                mainActivity.updateBackgroundGallaryImage(String.valueOf(imageUri));
            }
        }
    }

    @Override
    public void onBackgroundImageClick(String backgroundFileName) {
        // Get the reference to the previewImageView
        MainActivity mainActivity = (MainActivity) getActivity();
        MainActivity.CurrentImg = backgroundFileName ;
        // Update the background image in the MainActivity
        if (mainActivity != null) {
            mainActivity.updateBackgroundImage(backgroundFileName);
        }
    }



    private void showColorPickerDialog() {
        ColorPickerDialog.Builder colorPickerDialog = new ColorPickerDialog
                .Builder(requireActivity())
                .setTitle("Pick Color")
                .setColorShape(ColorShape.SQAURE)
                .setColorListener((color, colorHex) -> {
                    Drawable drawable = new ColorDrawable(color);
                    imageView.setImageDrawable(drawable);
                    Log.d(TAG, "onColorSelected: color" + color);
                });
        colorPickerDialog.show();
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChanged() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (imageView != null && imageView.getDrawable() != null) {
                    applyBlur(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optionally, you can perform actions when the user stops moving the seek bar
            }
        };
    }
    private void applyBlur(float blurRadius) {
        if (imageView.getDrawable() instanceof BitmapDrawable) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if(CurrentImg != null) {
                assert mainActivity != null;
                mainActivity.updateBackgroundImage(CurrentImg);
            }
            Bitmap originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap blurredBitmap = blurProcessor.blur(originalBitmap, blurRadius);
            imageView.setImageBitmap(blurredBitmap);
            lastProgress = blurRadius;

            originalBitmap1 = blurredBitmap;
        }

    }

}
