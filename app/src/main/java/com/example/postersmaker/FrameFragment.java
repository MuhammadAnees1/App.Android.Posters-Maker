package com.example.postersmaker;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class FrameFragment extends Fragment implements FrameImageBackGroundAdapter.FrameImageClickListener {
    private FrameImageBackGroundAdapter mainImageBackGroundAdapter;
    RecyclerView recyclerView;
    SeekBar seekBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame, container, false);

        recyclerView = view.findViewById(R.id.SetBackGroundLayout1);
        seekBar = view.findViewById(R.id.FrameOpacity);


        if (MainActivity.container.getVisibility() == View.GONE || MainActivity.container.getVisibility() == View.INVISIBLE) {
            MainActivity.container.setVisibility(View.VISIBLE);
//            MainActivity.container.startAnimation(MainActivity.fadeIn);

        }
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        mainImageBackGroundAdapter = new FrameImageBackGroundAdapter(getActivity(), getBackgroundList(getContext()), this);
        recyclerView.setAdapter(mainImageBackGroundAdapter);
        seekBar.setProgress(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float opacity = progress / 100f;
                if (MainActivity.imageView2 != null) {
                    MainActivity.imageView2.setAlpha(opacity);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }
        });

        return view;
    }
    private List<String> getBackgroundList(Context context) {
        List<String> backGroundList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();

        try {
            // List all files in the "Basic" directory inside the "assets" folder
            String[] backgroundFiles = assetManager.list("Basic");

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
    public void onFrameImageClick(String FrameFileName) {
        // Get the reference to the previewImageView
        MainActivity mainActivity = (MainActivity) getActivity();
        // Update the background image in the MainActivity
        if (mainActivity != null) {
            mainActivity.createImageLayout(null,FrameFileName,300,300);

        }
    }
}