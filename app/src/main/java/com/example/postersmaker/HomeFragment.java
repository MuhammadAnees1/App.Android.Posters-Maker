package com.example.postersmaker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements EditTextAdapter.OnItemSelected, TypeTextAdapter.onToolSelecteds {
    TextLayout selectedLayer;
    MainActivity activity;
    FrameLayout frameLayout;
    SeekBar seekBar;
    Handler handler;
    TextView buttonApplyFont;
    RelativeLayout text_buttonsUp , FontsLayout;
    RecyclerView recyclerView, TypeTextLayout;
    Button UpButton, downButton, leftButton, rightButton ,editButton ;
    private final EditTextAdapter editTextAdapter = new EditTextAdapter(this);
    private final TypeTextAdapter typeTextAdapter = new TypeTextAdapter(this);

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
        FontsLayout = view.findViewById(R.id.FontsLayout);
        buttonApplyFont = view.findViewById(R.id.font1);
        recyclerView = view.findViewById(R.id.editTextLayout);
        TypeTextLayout = view.findViewById(R.id.TypeTextLayout);

        handler = new Handler();
        activity = (MainActivity) requireActivity();
        selectedLayer = activity.selectedLayer;
         frameLayout = activity.frameLayout;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(editTextAdapter);


        TypeTextLayout.setLayoutManager(new GridLayoutManager(getContext(), 3));
        TypeTextLayout.setAdapter(typeTextAdapter);
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
    public void onToolSelected(ToolTypesForEditAdaptor toolType) {
        switch (toolType) {

            case Control:
                text_buttonsUp.setVisibility(View.VISIBLE);
                TypeTextLayout.setVisibility(View.GONE);
                break;
            case Style:
                TypeTextLayout.setVisibility(View.VISIBLE);
                text_buttonsUp.setVisibility(View.GONE);
                break;
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                TextSyleFragment textSyleFragment = new TextSyleFragment();
//                fragmentTransaction.replace(R.id.fragment_container, textSyleFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

            case text_size:
                seekBar.setVisibility(View.VISIBLE);
                // Set initial text size
                seekBar.setProgress((int) selectedLayer.getTextView().getTextSize());
                // Add a listener to the SeekBar to adjust the text size in real-time
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Update the text size based on the SeekBar progress
                        activity.selectedLayer.getTextView().setTextSize(progress);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Handle seek bar touch start
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Handle seek bar touch end
                    }
                });
                        break;
            case Fonts:
                FontsLayout.setVisibility(View.VISIBLE);
                text_buttonsUp.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);

                buttonApplyFont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Typeface customFont1 = ResourcesCompat.getFont(activity, R.font.abril_fatface);
                        selectedLayer.getTextView().setTypeface(customFont1);
                    }
                });
                        break;
            case Shadow:

                seekBar.setVisibility(View.VISIBLE);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Add logic to change the shadow of the selectedLayer's TextView based on the progress value
                        float radius = progress;
                        float dx = progress;
                        float dy = progress;
                        selectedLayer.getTextView().setShadowLayer(radius, dx, dy, Color.BLACK);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Add logic if needed when the user starts tracking touch on the SeekBar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Add logic if needed when the user stops tracking touch on the SeekBar
                    }
                });
                break;
            default:
                FontsLayout.setVisibility(View.GONE);
                text_buttonsUp.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
                TypeTextLayout.setVisibility(View.GONE);
                break;
        }
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

    @Override
    public void onToolSelected(ToolTypesForTypeTextAdaptor toolType) {

    }
}