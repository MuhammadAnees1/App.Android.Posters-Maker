package com.example.postersmaker;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.graphics.Color;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class HomeFragment extends Fragment implements EditTextAdapter.OnItemSelected {
    TextLayout selectedLayer;
    MainActivity activity;
    SeekBar seekBar;
    Handler handler;
    RelativeLayout text_buttonsUp;
    Button UpButton, downButton, leftButton, rightButton ,editButton ;

    private final EditTextAdapter editTextAdapter = new EditTextAdapter(this);
    public HomeFragment(){
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        editButton = view.findViewById(R.id.EditButton);
        UpButton = view.findViewById(R.id.UpButton);
        downButton = view.findViewById(R.id.DownButton);
        leftButton = view.findViewById(R.id.LeftButton);
        rightButton = view.findViewById(R.id.RightButton);
        text_buttonsUp = view.findViewById(R.id.Text_buttonsUp);
        seekBar = view.findViewById(R.id.seekBarFor);


        handler = new Handler();
        activity = (MainActivity) requireActivity();
        RecyclerView recyclerView = view.findViewById(R.id.editTextLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(editTextAdapter);
        editButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (activity.selectedLayer != null) {
                        selectedLayer = activity.selectedLayer;
                        TextHandlerClass.edittextDialog(getContext(), selectedLayer.getTextView());
                    }
                }
                return true;
            }
        });
        UpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if (activity.selectedLayer != null) {
                            selectedLayer = activity.selectedLayer;
                            moveFrameLayoutUpContinuously();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Stop moving the FrameLayout
                        stopMovingFrameLayout();
                        break;
                }
                return true;
            }
        });
        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (activity.selectedLayer != null) {
                            selectedLayer = activity.selectedLayer;
                            moveFrameLayoutDownContinuously();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Stop moving the FrameLayout
                        stopMovingFrameLayout();
                        break;
                }
                return true;
            }
        });
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (activity.selectedLayer != null) {
                            selectedLayer = activity.selectedLayer;
                            moveFrameLayoutLeftContinuously();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        stopMovingFrameLayout();
                        break;
                }
                return true;
            }
        });
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (activity.selectedLayer != null) {
                            selectedLayer = activity.selectedLayer;
                            moveFrameLayoutRightContinuously();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
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
        switch (toolType) {
            case text_size:
                text_buttonsUp.setVisibility(View.GONE);
                seekBar.setVisibility(View.VISIBLE);

                // Set initial text size

                selectedLayer = activity.selectedLayer;
                seekBar.getProgress();
                seekBar.setMax(selectedLayer.frameLayout.getHeight());
                // Calculate the maximum text size that fits within the FrameLayout


                // Add a listener to the SeekBar to adjust the text size in real-time
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Ensure that the text size does not exceed the maximum limit
                        if (progress < activity.selectedLayer.frameLayout.getHeight()) {


                        activity.selectedLayer.getTextView().setTextSize(progress);}
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                        selectedLayer = activity.selectedLayer;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        selectedLayer = activity.selectedLayer;
                    }
                });
                break;
            case Colour:

            default:
                // Unselecting the text_size button, so set seekBar to GONE and text_buttonsUp to VISIBLE
                seekBar.setVisibility(View.GONE);
                text_buttonsUp.setVisibility(View.VISIBLE);
        }
    }

    private void calculateNewTextSize(int progress) {
        // Retrieve the current text size
        float currentTextSize = selectedLayer.getTextView().getTextSize();

        // Calculate the new text size based on the progress
        float newSize = currentTextSize + (progress * 0.1f); // Adjust the multiplier as needed

        // Ensure that the new text size is within the desired range
        float minTextSize = 45.0f;  // Minimum text size
        float maxTextSize = 300.0f; // Maximum text size

        newSize = Math.max(minTextSize, Math.min(newSize, maxTextSize));

        // Apply the new text size
        selectedLayer.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);

        // Notify the parent view to request a layout pass
        selectedLayer.getFrameLayout().requestLayout();
    }









    private void moveFrameLayoutUpContinuously() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedLayer.setY(selectedLayer.getY()-5);
                handler.postDelayed(this, 10);
            }
        }, 50);
    }
    private void  moveFrameLayoutDownContinuously(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedLayer.setY(selectedLayer.getY()+5);
                handler.postDelayed(this, 10);
            }
        }, 50);
    }
    private void  moveFrameLayoutLeftContinuously(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedLayer.setX(selectedLayer.getX()-5);
                handler.postDelayed(this, 10);
            }
        }, 50);
    }
    private void  moveFrameLayoutRightContinuously(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedLayer.setX(selectedLayer.getX()+5);
                handler.postDelayed(this, 10);
            }
        }, 50);
    }
    private void stopMovingFrameLayout() {
        if (selectedLayer != null) {
            // Stop the continuous movement
            handler.removeCallbacksAndMessages(null);}
    }

}