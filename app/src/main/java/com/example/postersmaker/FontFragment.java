package com.example.postersmaker;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FontFragment extends Fragment implements FontAdapter.FontClickListener {

    private FontAdapter fontAdapter;

    public FontFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_font, container, false);

        ShimmerRecyclerView recyclerView = view.findViewById(R.id.fontRecyclerView);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        fetchAndSetFonts(getContext(), recyclerView);
        return view;
    }

    public void fetchAndSetFonts(final Context context, final RecyclerView recyclerView) {
        // Check if the font directory exists
        File fontDir = new File(context.getFilesDir(), "fonts");
        if (fontDir.exists() && fontDir.isDirectory()) {
            // If the font directory exists, directly load fonts from the directory
            List<Font> fontList = loadFontsFromDirectory(fontDir);
            if (fontList != null && !fontList.isEmpty()) {
                // If fonts are loaded successfully, update the RecyclerView adapter
                fontAdapter = new FontAdapter(context, fontList, FontFragment.this);
                recyclerView.setAdapter(fontAdapter);
                return;
            }
        }

        // If the font directory doesn't exist or fonts couldn't be loaded, fetch fonts from API
        FontApi.getFontListFromApi(context, new FontApi.OnFontListReceivedListener() {
            @Override
            public void onFontListReceived(List<Font> fontList) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the RecyclerView adapter with the fetched font list
                        fontAdapter = new FontAdapter(context, fontList, FontFragment.this);
                        recyclerView.setAdapter(fontAdapter);
                    }
                });
            }
        });
    }

    private List<Font> loadFontsFromDirectory(File fontDir) {
        List<Font> fontList = new ArrayList<>();
        File[] fontFiles = fontDir.listFiles();
        if (fontFiles != null) {
            for (File fontFile : fontFiles) {
                // Create Font object and add it to the list
                Font font = new Font();
                font.setName("Font " + (fontList.size() + 1) );
                font.setFilePath(fontFile.getAbsolutePath());
                // You might need to extract font name from file name
                fontList.add(font);
            }
        }
        return fontList;
    }

    @Override
    public void onFontClick(Font font) {
        Typeface typeface = Typeface.createFromFile(new File(font.getFilePath()));
        MainActivity.selectedLayer.getTextView().setTypeface(typeface);
    }

}
