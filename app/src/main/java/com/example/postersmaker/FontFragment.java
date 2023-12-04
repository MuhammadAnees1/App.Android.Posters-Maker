package com.example.postersmaker;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FontFragment extends Fragment implements FontAdapter.FontClickListener {

    private FontAdapter fontAdapter;


    public FontFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_font, container, false);

        // Replace with your actual font data



        RecyclerView recyclerView = view.findViewById(R.id.fontRecyclerView);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        fontAdapter = new FontAdapter(getActivity(), HomeFragment.getFontList(), this);
        recyclerView.setAdapter(fontAdapter);

        return view;
    }


    @Override
    public void onFontClick(Integer fontResourceId) {

        Typeface typeface = ResourcesCompat.getFont(requireContext(), fontResourceId);
        MainActivity activity = (MainActivity) requireActivity();
        if ( activity.selectedLayer != null) {
            activity.selectedLayer.getTextView().setTypeface(typeface);
        }
    }


}
