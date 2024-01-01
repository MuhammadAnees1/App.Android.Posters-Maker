package com.example.postersmaker;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EffectFragment extends Fragment implements BackGroundEffectsAdapter.BackgroundEffectClickListener {
RecyclerView recyclerView;
    BackGroundEffectsAdapter backGroundEffectsAdapter;
    SeekBar OpacitySeekBar;
    public EffectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_effect, container, false);
        if (MainActivity.container.getVisibility() == View.GONE || MainActivity.container.getVisibility() == View.INVISIBLE) {
            MainActivity.container.setVisibility(View.VISIBLE);
//            MainActivity.container.startAnimation(MainActivity.fadeIn);
        }
        recyclerView = view.findViewById(R.id.SetEffectLayout);
        OpacitySeekBar = view.findViewById(R.id.OpacitySeekBar);

        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        backGroundEffectsAdapter = new BackGroundEffectsAdapter(getActivity(), getEffectList(getContext()), this);
        recyclerView.setAdapter(backGroundEffectsAdapter);

        OpacitySeekBar.setProgress(100);
        OpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.OpacityBackground(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }
    private List<String> getEffectList(Context context) {
        List<String> backGroundList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            // List all files in the "Cover" directory inside the "assets" folder
            String[] backgroundFiles = assetManager.list("effect");

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
    public void onBackgroundEffectClick(String backgroundFileName) {
            // Get the reference to the previewImageView
            MainActivity mainActivity = (MainActivity) getActivity();
            // Update the background image in the MainActivity
            if (mainActivity != null) {
                mainActivity.applyEffectOnBackgroundImage(backgroundFileName);
            }
        }
    }