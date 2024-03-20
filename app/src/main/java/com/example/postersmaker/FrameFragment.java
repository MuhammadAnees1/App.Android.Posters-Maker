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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FrameFragment extends Fragment implements FrameImageBackGroundAdapter.FrameImageClickListener {
    public FrameImageBackGroundAdapter mainImageBackGroundAdapter;
    ShimmerRecyclerView recyclerView;
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
        fetchAndSetframe(getContext(), recyclerView);

        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float opacity = progress / 100f;
                if(MainActivity.selectedLayer1 != null) {
                    MainActivity.selectedLayer1.getImageView().setAlpha(opacity);
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
    public void fetchAndSetframe(final Context context, final RecyclerView recyclerView) {
        // Check if the font directory exists
        File frameDir = new File(context.getFilesDir(), "frames");
        if (frameDir.exists() && frameDir.isDirectory()) {
            // If the font directory exists, directly load fonts from the directory
            List<Frame> frameList = loadFramesFromDirectory(frameDir);
            if (frameList != null && !frameList.isEmpty()) {
                // If fonts are loaded successfully, update the RecyclerView adapter
                mainImageBackGroundAdapter = new FrameImageBackGroundAdapter(context, frameList, FrameFragment.this);
                recyclerView.setAdapter(mainImageBackGroundAdapter);
                return;
            }
        }

        // If the font directory doesn't exist or fonts couldn't be loaded, fetch fonts from API
        FrameApi.getFrameListFromApi(context, new FrameApi.OnFrameListReceivedListener() {
            @Override
            public void onFrameListReceived(List<Frame> frameList) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the RecyclerView adapter with the fetched font list
                        mainImageBackGroundAdapter = new FrameImageBackGroundAdapter(context, frameList, FrameFragment.this);
                        recyclerView.setAdapter(mainImageBackGroundAdapter);
                    }
                });
            }
        });
    }
    private List<Frame> loadFramesFromDirectory(File frameDir) {
        List<Frame> FrameList = new ArrayList<>();
        File[] frameFiles = frameDir.listFiles();
        if (frameFiles != null) {
            for (File fontFile : frameFiles) {
                // Create Font object and add it to the list
                Frame frame = new Frame();
                frame.setName("frame " + (FrameList.size() + 1) );
                frame.setFilePath(fontFile.getAbsolutePath());
                // You might need to extract font name from file name
                FrameList.add(frame);
            }
        }
        return FrameList;
    }

    @Override
    public void onFrameImageClick(String frameFilePath) {
        // Get the reference to the previewImageView
        MainActivity mainActivity = (MainActivity) getActivity();
        // Update the background image in the MainActivity
        if (mainActivity != null) {
            mainActivity.createImageLayout(null, null, frameFilePath, 200, 200);
            MainActivity.frameContainer.setVisibility(View.VISIBLE);
        }
    }
}