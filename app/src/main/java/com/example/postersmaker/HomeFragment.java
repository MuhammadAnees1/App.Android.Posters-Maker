package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements EditTextAdapter.OnItemSelected {
    FrameLayout frameLayout;
     Handler handler;

    private final EditTextAdapter editTextAdapter = new EditTextAdapter(this);

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

         handler = new Handler();


        MainActivity activity = (MainActivity) requireActivity();

        frameLayout = activity.getFrameLayout();
        Log.d(TAG, "onCreateView: " + frameLayout);

        RecyclerView recyclerView = view.findViewById(R.id.editTextLayout);
        Button UpButton = view.findViewById(R.id.UpButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(editTextAdapter);

// ...

        UpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start moving the FrameLayout upwards here continuously
                        moveFrameLayoutUpContinuously();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Stop moving the FrameLayout
                        stopMovingFrameLayout();
                        break;
                }
                return true;
            }
        });
        return view;
    }
    @Override
    public void onToolSelected(ToolTypes toolType) {
        // Implement this method as needed
    }
    private void moveFrameLayoutUpContinuously() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentY = (int) frameLayout.getY();
                int newY = currentY - 5;
                frameLayout.setY(newY);
                handler.postDelayed(this, 10);
            }
        }, 50);
    }
    private void stopMovingFrameLayout() {
        if (frameLayout.getY() <= 0) {
            return;
        }
        // Stop the continuous movement
        handler.removeCallbacksAndMessages(null);

    }
}
