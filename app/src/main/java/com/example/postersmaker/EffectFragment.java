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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EffectFragment extends Fragment implements BackGroundEffectsAdapter.BackgroundEffectClickListener {
ShimmerRecyclerView recyclerView;
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
        fetchAndSetFilters(getContext(), recyclerView);

        OpacitySeekBar.setProgress(100);
        OpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if(MainActivity.filterView != null){
                MainActivity.filterView.setAlpha(progress / 100f);
                }
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
    public void fetchAndSetFilters(final Context context, final RecyclerView recyclerView) {
        // Check if the font directory exists
        File filterDir = new File(context.getFilesDir(), "filters");
        if (filterDir.exists() && filterDir.isDirectory()) {
            // If the font directory exists, directly load fonts from the directory
            List<Filter> filterList = loadFiltersFromDirectory(filterDir);
            if (filterList != null && !filterList.isEmpty()) {
                // If fonts are loaded successfully, update the RecyclerView adapter
                backGroundEffectsAdapter = new BackGroundEffectsAdapter(context, filterList, EffectFragment.this);
                recyclerView.setAdapter(backGroundEffectsAdapter);
                return;
            }
        }
        FilterApi.getFilterListFromApi(context, new FilterApi.OnFilterListReceivedListener() {
            @Override
            public void onFilterListReceived(List<Filter> filterList) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the RecyclerView adapter with the fetched font list
                        backGroundEffectsAdapter = new BackGroundEffectsAdapter(context, filterList, EffectFragment.this);
                        recyclerView.setAdapter(backGroundEffectsAdapter);
                    }
                });
            }

        });
    }
    private List<Filter> loadFiltersFromDirectory(File filterDir) {
        List<Filter> FilterList = new ArrayList<>();
        File[] filterFiles = filterDir.listFiles();
        if (filterFiles != null) {
            for (File filterFile : filterFiles) {
                // Create Font object and add it to the list
                Filter filter = new Filter();
                filter.setName("Filter " + (FilterList.size() + 1) );
                filter.setFilePath(filterFile.getAbsolutePath());
                // You might need to extract font name from file name
                FilterList.add(filter);
            }
        }
        return FilterList;
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