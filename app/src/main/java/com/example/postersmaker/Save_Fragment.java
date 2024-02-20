package com.example.postersmaker;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Save_Fragment extends Fragment {
        Button save480px, save720px, save1080px;
    public Save_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_, container, false);
        save480px = view.findViewById(R.id.save480px);
        save720px = view.findViewById(R.id.save720px);
        save1080px = view.findViewById(R.id.save1080px);
        save480px.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            mainActivity.performStorageOperation(480);

        });
        save720px.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            mainActivity.performStorageOperation(720);

        });
        save1080px.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            mainActivity.performStorageOperation(1080);

        });


        return view;
    }
}