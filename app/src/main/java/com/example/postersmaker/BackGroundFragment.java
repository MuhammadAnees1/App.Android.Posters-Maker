package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.postersmaker.ImagePickerManager.addImageToContainer;
import static com.example.postersmaker.MainActivity.CurrentImg;
import static com.example.postersmaker.MainActivity.imageView;
import static com.example.postersmaker.MainActivity.originalBitmap1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BackGroundFragment extends Fragment implements MainImageBackGroundAdapter.BackgroundImageClickListener {
    MainImageBackGroundAdapter mainImageBackGroundAdapter;

    ShimmerRecyclerView recyclerView;
    private static final int IMAGE_PICK_REQUEST = 101;

    static float lastProgress = 0;
    FrameLayout filterContainer;
    private BlurProcessor blurProcessor;

     SeekBar blurSeekBar, OpacityBackgroundSeekbar;
    ImageView PickBackGroundButton, PickBackGroundColor, FilterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_back_ground, container, false);

        recyclerView = view.findViewById(R.id.SetBackGroundLayout);
        PickBackGroundButton = view.findViewById(R.id.PickBackGroundButton);
        PickBackGroundColor = view.findViewById(R.id.PickBackGroundColor);
        blurSeekBar = view.findViewById(R.id.blurSeekBar);
        FilterButton = view.findViewById(R.id.FilterButton);
        filterContainer = view.findViewById(R.id.Filter);
        OpacityBackgroundSeekbar = view.findViewById(R.id.OpacityBackground);
        blurProcessor = new BlurProcessor(requireActivity());
        if (MainActivity.container.getVisibility() == View.GONE || MainActivity.container.getVisibility() == View.INVISIBLE) {
            MainActivity.container.setVisibility(View.VISIBLE);
//            MainActivity.container.startAnimation(MainActivity.fadeIn);
        }
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        PickBackGroundColor.setOnClickListener(v -> showColorPickerDialog());
        fetchAndSetBackground(requireContext(), recyclerView);

        blurSeekBar.setOnSeekBarChangeListener(onSeekBarChanged());
        blurSeekBar.setProgress((int) lastProgress);
        FilterButton.setOnClickListener(v -> {
            if(filterContainer.getVisibility() == View.VISIBLE){
                filterContainer.setVisibility(View.GONE);
            }
            else{
            filterContainer.setVisibility(View.VISIBLE);
            EffectFragment effectFragment = new EffectFragment();
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Filter, effectFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            }
        });
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
            MainActivity.backimg = true;
            ImagePickerManager.openGallery(getActivity());
});
        return view;
    }

    public void fetchAndSetBackground(final Context context, final RecyclerView recyclerView) {
        // Check if the font directory exists
        File backgroundDir = new File(context.getFilesDir(), "backgrounds");
        if (backgroundDir.exists() && backgroundDir.isDirectory()) {
            // If the font directory exists, directly load fonts from the directory
            List<Background> backgroundList = loadbackgroundFromDirectory(backgroundDir);
            if (backgroundList != null && !backgroundList.isEmpty()) {
                // If fonts are loaded successfully, update the RecyclerView adapter
                mainImageBackGroundAdapter = new MainImageBackGroundAdapter(context, backgroundList, BackGroundFragment.this);
                recyclerView.setAdapter(mainImageBackGroundAdapter);
                return;
            }
        }

        // If the font directory doesn't exist or fonts couldn't be loaded, fetch fonts from API
        BackgroundApi.getBackgroundListFromApi(context, new BackgroundApi.OnBackgroundListReceivedListener() {
            @Override
            public void onBackgroundListReceived(List<Background> backgroundList) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the RecyclerView adapter with the fetched font list
                        mainImageBackGroundAdapter = new MainImageBackGroundAdapter(context, backgroundList, BackGroundFragment.this);
                        recyclerView.setAdapter(mainImageBackGroundAdapter);
                    }
                });
            }
        } );

    }
    private List<Background> loadbackgroundFromDirectory(File backgroundDir) {
        List<Background> backgroundList = new ArrayList<>();
        File[] backgroundFiles = backgroundDir.listFiles();
        if (backgroundFiles != null) {
            for (File backgroundFile : backgroundFiles) {
                // Create Font object and add it to the list
                Background background = new Background();
                background.setName("Background " + (backgroundList.size() + 1) );
                background.setFilePath(backgroundFile.getAbsolutePath());
                // You might need to extract font name from file name
                backgroundList.add(background);
            }
        }
        return backgroundList;
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



    @Override
    public void onBackgroundImageClick(String path) {
        // Get the reference to the previewImageView
        MainActivity mainActivity = (MainActivity) getActivity();
        MainActivity.CurrentImg = path ;
        // Update the background image in the MainActivity
        if (mainActivity != null) {
            mainActivity.updateBackgroundImage(path);
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
        Bitmap originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        if (imageView.getDrawable() instanceof BitmapDrawable) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if(CurrentImg != null) {
                assert mainActivity != null;
                mainActivity.updateBackgroundImage(CurrentImg);
                originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            }

            else if(MainActivity.currenBit != null) {
                originalBitmap = MainActivity.currenBit;
                Toast.makeText(mainActivity, "done", Toast.LENGTH_SHORT).show();
                }
            }
                Bitmap blurredBitmap = blurProcessor.blur(originalBitmap, blurRadius);
                imageView.setImageBitmap(blurredBitmap);
                lastProgress = blurRadius;
                originalBitmap1 = blurredBitmap;
        }

    }


