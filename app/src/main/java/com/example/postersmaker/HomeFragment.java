package com.example.postersmaker;

import static android.app.ProgressDialog.show;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements EditTextAdapter.OnItemSelected, TypeTextAdapter.onToolSelecteds,HueAdapter.OnHueItemClickListener {
    static TranslateAnimation fadeIn;
    TextLayout selectedLayer;
    ImageLayout selectedLayer1;
    MainActivity activity;
    private Drawable[] hueDrawables;
    private int currentHueIndex = 0;
    private HueAdapter hueAdapter;
    private List<HueItem> hueItemList;
    FrameLayout frameLayout,fragmentContainer1, fontContainer;
    List<Integer> colors = getYourColorList();
    float lastSetTextSize = 1f;
    float initialTextSize;
    ColorPickerFragment colorPickerFragment;
    TextView lineStrokeButton, dashStrokeButton, dotStrokeButton;
    static SeekBar seekBar , spaceSeekBar,opacitySeekBar, sizeSeekBar;
    Handler handler;
    TextView buttonApplyFont;
    String currentText;
    LinearLayout text_buttonsUp,StrokeLayout, hueLayout;
    private StrokeType currentStrokeType = StrokeType.LINE;
    static RecyclerView recyclerView, filterRecycleView;
    RecyclerView TypeTextLayout;
    Button UpButton, downButton, leftButton, rightButton ,editButton,flipButton, copybutton ;
    static Button Image_control_button,Image_control_opacity, Image_filter;
    Paint textPaint;
    private Paint strokePaint;
   final EditTextAdapter editTextAdapter = new EditTextAdapter(this);
    private final TypeTextAdapter typeTextAdapter = new TypeTextAdapter(this);
    boolean openedFromImagePickerManager = false;
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
        hueLayout = view.findViewById(R.id.hueLayout);
        spaceSeekBar = view.findViewById(R.id.seekBarForspace);
        opacitySeekBar = view.findViewById(R.id.seekBarForOpacity);
        sizeSeekBar = view.findViewById(R.id.seekBarForSize);
        seekBar.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.editTextLayout);
        TypeTextLayout = view.findViewById(R.id.TypeTextLayout);
        StrokeLayout = view.findViewById(R.id.StrokeLayout);
        lineStrokeButton = view.findViewById(R.id.LineStroke);
        copybutton = view.findViewById(R.id.copy_button);
        dashStrokeButton = view.findViewById(R.id.DashStroke);
        dotStrokeButton = view.findViewById(R.id.DotStroke);
        fragmentContainer1 = view.findViewById(R.id.fragment_container1);
        fontContainer = view.findViewById(R.id.font_container);
        Image_control_button= view.findViewById(R.id.Image_control_button);
        Image_control_opacity= view.findViewById(R.id.Image_control_opacity);
        Image_filter = view.findViewById(R.id.filterSet);
        flipButton = view.findViewById(R.id.FlipButton);


        filterRecycleView = view.findViewById(R.id.hueRecycler);

        // Initialize hueItemList with different hue values
        hueItemList = new ArrayList<>();


        // Initialize and set up the RecychueItemList.add(new HueItem(-90));lerView

        Bundle arguments = getArguments();
        if (arguments != null) {
            openedFromImagePickerManager = arguments.getBoolean("openedFromImagePickerManager", true);
        }
        if (openedFromImagePickerManager) {
            setDefaultState();
            Image_control_button.setVisibility(View.VISIBLE);
            Image_control_opacity.setVisibility(View.VISIBLE);
            Image_filter.setVisibility(View.VISIBLE);
            flipButton.setVisibility(View.VISIBLE);
            }

        else {
            if(recyclerView.getVisibility() != View.VISIBLE) {
                recyclerView.setVisibility(View.VISIBLE);
                flipButton.setVisibility(View.GONE);
            }
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setHomeFragment(this);
        }
        fadeIn = new TranslateAnimation(0, 0, 400, 0);
        fadeIn.setDuration(400);

        textPaint = new Paint();
        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);

        handler = new Handler();
        activity = (MainActivity) requireActivity();
        frameLayout = activity.frameLayout;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(editTextAdapter);

        TypeTextLayout.setLayoutManager(new GridLayoutManager(getContext(), 3));
        TypeTextLayout.setAdapter(typeTextAdapter);

        Image_control_opacity.setOnClickListener(v -> {
            setDefaultState();
            Image_control_button.setVisibility(View.VISIBLE);
            Image_control_opacity.setVisibility(View.VISIBLE);
            Image_filter.setVisibility(View.VISIBLE);
                opacitySeekBar.setVisibility(View.VISIBLE);

        });
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.selectedLayer != null){

                    if (MainActivity.selectedLayer.getTextView() != null) {
                        if(!MainActivity.selectedLayer.isFlip()){
                            Track.list.add(new Track(null,MainActivity.selectedLayer.getId(),1,true));
                            Track.list2.clear();
                            MainActivity.selectedLayer.getTextView().setScaleX(-1f);
                        MainActivity.selectedLayer.setFlip(true);}
                        else{
                            Track.list.add(new Track(null,MainActivity.selectedLayer.getId(),-1,true));
                            Track.list2.clear();

                            MainActivity.selectedLayer.getTextView().setScaleX(1f);
                            MainActivity.selectedLayer.setFlip(false);}

                    }
                }
                else{
                selectedLayer1 = MainActivity.selectedLayer1;
                    float currentScaleX = MainActivity.selectedLayer1.getImageView().getScaleX();
                    Track.list.add(new Track(MainActivity.selectedLayer1.getImageView(),MainActivity.selectedLayer1.getId(),MainActivity.selectedLayer1.getImageView().getScaleX(),true));
                Track.list2.clear();

                selectedLayer1.getImageView().setScaleX(-currentScaleX);
                    Toast.makeText(activity, "Image flipped", Toast.LENGTH_SHORT).show();
            }}

        });
        Image_filter.setOnClickListener(v -> {

            if(MainActivity.selectedLayer1 != null ){

                setDefaultState();
                Image_control_button.setVisibility(View.VISIBLE);
                Image_control_opacity.setVisibility(View.VISIBLE);
                Image_filter.setVisibility(View.VISIBLE);
                hueLayout.setVisibility(View.VISIBLE);
                filterRecycleView.setVisibility(View.VISIBLE);
                huelistUpdate();
                hueAdapter = new HueAdapter(hueItemList);
                filterRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                filterRecycleView.setAdapter(hueAdapter);
            }
        });


        Image_control_button.setOnClickListener(v -> {
            if(text_buttonsUp.getVisibility() != View.VISIBLE){
                setDefaultState();
                Image_control_button.setVisibility(View.VISIBLE);
                Image_control_opacity.setVisibility(View.VISIBLE);
                Image_filter.setVisibility(View.VISIBLE);
                text_buttonsUp.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                flipButton.setVisibility(View.VISIBLE);
//                    text_buttonsUp.startAnimation(activity.fadeIn);

            }
        });
        opacitySeekBar.setProgress(100); // Set the initial progress to 100
        opacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float opacity = progress / 100f; // Convert progress to a float between 0 and 1
                selectedLayer1 = MainActivity.selectedLayer1;

                // Ensure that selectedLayer1 and its ImageView are not null
                if (selectedLayer1 != null && selectedLayer1.getImageView() != null) {
                    selectedLayer1.getImageView().setAlpha(opacity);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            Track.list.add(new Track(MainActivity.selectedLayer1.getId(),true,MainActivity.selectedLayer1.getImageView().getAlpha()));
                Track.list2.clear();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }
        });
        copybutton.setOnClickListener(v -> {
          Copybutton.onCopy(activity);

        });


        editButton.setOnTouchListener((view1, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (MainActivity.selectedLayer != null) {
                    selectedLayer = MainActivity.selectedLayer;
                    TextHandlerClass.edittextDialog(getContext(), selectedLayer.getTextView());
                }
            }
            return true;
        });
        UpButton.setOnTouchListener((view12, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:


                    if (MainActivity.selectedLayer != null) {
                        selectedLayer = MainActivity.selectedLayer;
                        Track.list.add(new Track(MainActivity.selectedLayer.getId(),MainActivity.selectedLayer.getFrameLayout().getX(), MainActivity.selectedLayer.getFrameLayout().getY(), true));
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
                        Track.list.add(new Track(MainActivity.selectedLayer1.getId(),MainActivity.selectedLayer1.getFrameLayout().getX(),MainActivity.selectedLayer1.getFrameLayout().getY(),true));

                    }
                    Track.list2.clear();
                    moveFrameLayoutUpContinuously();
                    break;
                case MotionEvent.ACTION_UP:
                    // Stop moving the FrameLayout
                    stopMovingFrameLayout();
                    break;
            }
            return true;
        });
        downButton.setOnTouchListener((view13, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (MainActivity.selectedLayer != null) {
                        selectedLayer = MainActivity.selectedLayer;
                        Track.list.add(new Track(MainActivity.selectedLayer.getId(),MainActivity.selectedLayer.getFrameLayout().getX(), MainActivity.selectedLayer.getFrameLayout().getY(), true));
                        Track.list2.clear();

                        moveFrameLayoutDownContinuously();
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
                        Track.list.add(new Track(MainActivity.selectedLayer1.getId(),MainActivity.selectedLayer1.getFrameLayout().getX(),MainActivity.selectedLayer1.getFrameLayout().getY(),true));
                        Track.list2.clear();

                        moveFrameLayoutDownContinuously();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // Stop moving the FrameLayout
                    stopMovingFrameLayout();
                    break;
            }
            return true;
        });
        leftButton.setOnTouchListener((view14, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (MainActivity.selectedLayer != null) {
                        selectedLayer = MainActivity.selectedLayer;
                        Track.list.add(new Track(MainActivity.selectedLayer.getId(),MainActivity.selectedLayer.getFrameLayout().getX(), MainActivity.selectedLayer.getFrameLayout().getY(), true));
                        Track.list2.clear();

                        moveFrameLayoutLeftContinuously();
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
                        Track.list.add(new Track(MainActivity.selectedLayer1.getId(),MainActivity.selectedLayer1.getFrameLayout().getX(),MainActivity.selectedLayer1.getFrameLayout().getY(),true));
                        Track.list2.clear();

                        moveFrameLayoutLeftContinuously();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    stopMovingFrameLayout();
                    break;
            }
            return true;
        });
        rightButton.setOnTouchListener((view15, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (MainActivity.selectedLayer != null) {
                        selectedLayer = MainActivity.selectedLayer;
                        Track.list.add(new Track(MainActivity.selectedLayer.getId(),MainActivity.selectedLayer.getFrameLayout().getX(), MainActivity.selectedLayer.getFrameLayout().getY(), true));
                        Track.list2.clear();

                        moveFrameLayoutRightContinuously();
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
                        Track.list.add(new Track(MainActivity.selectedLayer1.getId(),MainActivity.selectedLayer1.getFrameLayout().getX(),MainActivity.selectedLayer1.getFrameLayout().getY(),true));
                        Track.list2.clear();

                        moveFrameLayoutRightContinuously();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    stopMovingFrameLayout();
                    break;
            }
            return true;
        });
        return view;
    }
//    private void onStrokeTypeSelected(StrokeType strokeType) {
//        currentStrokeType = strokeType;
//        if (MainActivity.selectedLayer != null) {
//            MainActivity.selectedLayer.setStrokeType(strokeType);
//        }
//    }
    private void setDefaultState() {
        // Set your default UI state here
        hueLayout.setVisibility(View.GONE);
        fontContainer.setVisibility(View.GONE);
        sizeSeekBar.setVisibility(View.GONE);
        fragmentContainer1.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        spaceSeekBar.setVisibility(View.GONE);
        opacitySeekBar.setVisibility(View.GONE);
        filterRecycleView.setVisibility(View.INVISIBLE);
        Image_control_button.setVisibility(View.GONE);
        Image_control_opacity.setVisibility(View.GONE);
        Image_filter.setVisibility(View.GONE);
        TypeTextLayout.setVisibility(View.GONE);
        text_buttonsUp.setVisibility(View.GONE);

        StrokeLayout.setVisibility(View.GONE);
    }
    @Override
    public void onToolSelected(ToolTypesForEditAdaptor toolType) {
        switch (toolType) {
            case Control:
                flipButton.setVisibility(View.VISIBLE);
                if(text_buttonsUp.getVisibility() != View.VISIBLE){
                    setDefaultState();
                    text_buttonsUp.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                    text_buttonsUp.startAnimation(fadeIn);
                }
                break;
            case Style:
                if(TypeTextLayout.getVisibility() != View.VISIBLE){
                    setDefaultState();
                    TypeTextLayout.setVisibility(View.VISIBLE);
                    TypeTextLayout.startAnimation(MainActivity.fadeIn);}
                break;
            case text_size:
                if(sizeSeekBar.getVisibility() != View.VISIBLE){
                    setDefaultState();
                    sizeSeekBar.setVisibility(View.VISIBLE);
                    sizeSeekBar.startAnimation(MainActivity.fadeIn);}

                // Set minimum and maximum text size
                float minTextSize = 10.0f;
                float maxTextSize = activity.pxTodp(120);

                // Set initial text size
                selectedLayer = MainActivity.selectedLayer;
                if(selectedLayer != null) {
                    initialTextSize = selectedLayer.getMaxSize();
                    lastSetTextSize = initialTextSize;

                    // Set the maximum and minimum values for the SeekBar
                    sizeSeekBar.setMax((int) (maxTextSize - minTextSize));
                    sizeSeekBar.setProgress((int) (lastSetTextSize - minTextSize));

                }
                // Add a listener to the SeekBar to adjust the text size in real-time
                sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Calculate the current text size based on progress

                        float textSize = minTextSize + progress;

                        // Ensure that the text size does not exceed the maximum limit
                        textSize = Math.max(minTextSize, Math.min(textSize, maxTextSize));

                        textSize = Math.min(textSize, initialTextSize);
                        // Set the text size
                        if(selectedLayer != null){
                        activity.selectedLayer.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);}
                        // Notify the parent view to request a layout pass
                        selectedLayer.getFrameLayout().requestLayout();

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Track.list.add(new Track(true,MainActivity.selectedLayer.getId(),MainActivity.selectedLayer.getTextView().getTextSize()));
                        Track.list2.clear();

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                break;
            case Fonts:
                if (fontContainer.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    fontContainer.setVisibility(View.VISIBLE);
                    fontContainer.startAnimation(MainActivity.fadeIn);
                }

                showFontSelectionFragment();
                break;
            case Shadow:
                if (seekBar.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    seekBar.setVisibility(View.VISIBLE);
                    seekBar.startAnimation(MainActivity.fadeIn);}

                float minShadow = 0.0f; // Set your minimum shadow value
                float maxShadow = 20.0f; // Set your maximum shadow value


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Constrain the progress within the desired range


                        // Map the progress value to the desired shadow range
                        float shadowValue = minShadow + (maxShadow - minShadow) * (progress / 100.0f);

                        // Apply the shadow to the selectedLayer's TextView
                        MainActivity.selectedLayer.getTextView().setShadowLayer(shadowValue, shadowValue, shadowValue, Color.BLACK);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Track.list.add(new Track(MainActivity.selectedLayer.getId(),
                                MainActivity.selectedLayer.getTextView().getShadowRadius(),
                                MainActivity.selectedLayer.getTextView().getShadowDx(),
                                MainActivity.selectedLayer.getTextView().getShadowDy(),
                                true
                        ));
                        Track.list2.clear();


                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                break;
            case Space:
                if (spaceSeekBar.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    spaceSeekBar.setVisibility(View.VISIBLE);
                    spaceSeekBar.startAnimation(MainActivity.fadeIn);
                }
                spaceSeekBar.setMax(activity.pxTodp(33));
                spaceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float letterSpacing = progress / (float)activity.pxTodp(33);
                        if (MainActivity.selectedLayer != null) {
                            MainActivity.selectedLayer.getTextView().setLetterSpacing(letterSpacing);
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Track.list.add(new Track(MainActivity.selectedLayer.getTextView().getLetterSpacing(),true,MainActivity.selectedLayer.getId()));
                        Track.list2.clear();

                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Handle touch event stop if needed
                    }
                });
                break;
//            case stroke:
//                lineStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.LINE));
//                dashStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.DASH));
//                dotStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.DOT));
//
//                if (StrokeLayout.getVisibility() != View.VISIBLE) {
//                    setDefaultState();
//                    StrokeLayout.setVisibility(View.VISIBLE);
//                    seekBar.setVisibility(View.VISIBLE);
//                    StrokeLayout.startAnimation(activity.fadeIn);
//                }
//
//                final float minStrokeWidth = 1.0f;
//                final float maxStrokeWidth = 10.0f;
//
//                final Paint strokePaint = activity.selectedLayer.getTextView().getPaint();
//                strokePaint.setStyle(Paint.Style.STROKE);
//
//                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        float limitedProgress = Math.min(Math.max(progress, 0), activity.pxTodp(40));
//                        float strokeWidth = minStrokeWidth + (maxStrokeWidth - minStrokeWidth) * (limitedProgress / (float) activity.pxTodp(40));
//                        strokePaint.setStrokeWidth(strokeWidth);
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                        // Add logic if needed when the user starts tracking touch on the SeekBar
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        // Add logic if needed when the user stops tracking touch on the SeekBar
//                    }
//                });
//
//                break;
            case Colour:
                if ( fragmentContainer1.getVisibility() != View.VISIBLE ) {
                    setDefaultState();
                    fragmentContainer1.setVisibility(View.VISIBLE);
                    fragmentContainer1.startAnimation(activity.fadeIn);
                }
                showColorPickerFragment(colors);
                break;
            default:// Unselecting the text_size button, so set seekBar to GONE and text_buttonsUp to VISIBLE
                setDefaultState();
        }
    }
    private void showFontSelectionFragment() {
        // Replace "your_fragment_container_id" with the actual ID of your fragment container
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        FontFragment fontFragment = new FontFragment();
        transaction.replace(R.id.font_container, fontFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showColorPickerFragment(List<Integer> colors) {
        ColorPickerFragment colorPickerFragment = ColorPickerFragment.newInstance(
                colors,
                new ColorPickerFragment.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        activity.selectedLayer.getTextView().setTextColor(color);
                    }
                }
        );
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer1.getId(), colorPickerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    static List<Integer> getFontList() {
        List<Integer> fontList = new ArrayList<>();
        fontList.add(R.font.abril_fatface);
        fontList.add(R.font.bangers);
        fontList.add(R.font.alex_brush);
        fontList.add(R.font.f1);
        fontList.add(R.font.f100);
        fontList.add(R.font.f11);
        fontList.add(R.font.f16);
        fontList.add(R.font.f17);
        fontList.add(R.font.f19);
        fontList.add(R.font.f21);
        fontList.add(R.font.f41);
        fontList.add(R.font.f46);
        fontList.add(R.font.f47);
        fontList.add(R.font.f48);
        fontList.add(R.font.f51);
        fontList.add(R.font.f52);
        fontList.add(R.font.f60);
        fontList.add(R.font.f67);
        fontList.add(R.font.f83);
        fontList.add(R.font.f92);
        fontList.add(R.font.f93);
        fontList.add(R.font.f98);
        return fontList;
    }
    void setDefaultStateFromExternal() {
        setDefaultState();
    }
    private void moveFrameLayoutUpContinuously() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.selectedLayer != null) {
                    selectedLayer.setY(selectedLayer.getY() - 3);
                    handler.postDelayed(this, 10);
                }else {
                    selectedLayer1.setY(selectedLayer1.getY() - 3);
                    handler.postDelayed(this, 10);
                }
            }
        }, 50);
    }
    private void  moveFrameLayoutDownContinuously(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.selectedLayer != null) {
                    selectedLayer.setY(selectedLayer.getY()+3);
                    handler.postDelayed(this, 10);
                } else {
                    selectedLayer1.setY(selectedLayer1.getY()+3);
                    handler.postDelayed(this, 10);
                }
            }
        }, 50);
    }
    private void  moveFrameLayoutLeftContinuously(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.selectedLayer != null) {
                    selectedLayer.setX(selectedLayer.getX() - 3);
                    handler.postDelayed(this, 10);
                } else {
                    selectedLayer1.setX(selectedLayer1.getX() - 3);
                    handler.postDelayed(this, 10);
                }
            }
        }, 50);
    }
    private void  moveFrameLayoutRightContinuously(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.selectedLayer != null) {
                    selectedLayer.setX(selectedLayer.getX() + 3);
                    handler.postDelayed(this, 10);
                } else {
                    selectedLayer1.setX(selectedLayer1.getX() +3);
                    handler.postDelayed(this, 10);
                }
            }
        }, 50);
    }
    private void stopMovingFrameLayout() {
        if (selectedLayer != null || selectedLayer1 != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
    static List<Integer> getYourColorList() {
        List<Integer> colors = new ArrayList<>();

        colors.add(0xFFED0A3F); // Red
        colors.add(0xFFE91E63); // Pink
        colors.add(0xFFFF2C93); //Light Pink
        colors.add(0xFF9C27B0); // Purple
        colors.add(0xFF673AB7); // DEEP PURPLE 500
        colors.add(0xFF3F51B5); // INDIGO 500
        colors.add(0xFF2196F3); // BLUE 500
        colors.add(0xFF03A9F4); // LIGHT BLUE 500
        colors.add(0xFF00BCD4); // CYAN 500
        colors.add(0xFF009688); // TEAL 500
        colors.add(0xFF4CAF50); // GREEN 500
        colors.add(0xFF8BC34A); // LIGHT GREEN 500
        colors.add(0xFFCDDC39); // LIME 500
        colors.add(0xFFFFEB3B); // YELLOW 500
        colors.add(0xFFFFC107); // AMBER 500
        colors.add(0xFFFF9800); // ORANGE 500
        colors.add(0xFF795548); // BROWN 500
        colors.add(0xFF607D8B); // BLUE GREY 500
        colors.add(0xFF9E9E9E); // GREY 500
        colors.add(0xFFFFFFFF); // WHITE
        colors.add(0xFF000000); // BLACK

        return colors;
    }

    @Override

    public void onToolSelected(ToolTypesForTypeTextAdaptor toolType) {
        Typeface currentTypeface = MainActivity.selectedLayer.getTextView().getTypeface();

        int a= 0, b= 0;
        switch (toolType) {
            case formatBold:
                // Toggle bold
                if (currentTypeface != null) {
                    if ((currentTypeface.getStyle() == Typeface.BOLD_ITALIC)) {
                        MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.ITALIC);
                    } else if ((currentTypeface.getStyle() == Typeface.BOLD)) {
                        MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.NORMAL);
                    } else if ((currentTypeface.getStyle() == Typeface.ITALIC)) {
                        MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD_ITALIC);
                    }

                }
                else {
                    MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD);
                }
                break;
            case FormatUnderlined:
                // Underline the text
                if ((MainActivity.selectedLayer.getTextView().getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) != 0) {
                    MainActivity.selectedLayer.getTextView().setPaintFlags(MainActivity.selectedLayer.getTextView().getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    Track.list.add(new Track(MainActivity.selectedLayer.getId(),true,true));
                    Track.list2.clear();

                } else {
                    MainActivity.selectedLayer.getTextView().setPaintFlags(MainActivity.selectedLayer.getTextView().getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    Track.list.add(new Track(MainActivity.selectedLayer.getId(),false,true));
                    Track.list2.clear();

                }
                break;
            case formatLeft:
                a =  MainActivity.selectedLayer.getTextView().getTextAlignment();
                if (a == 2 || a ==1){ b = 0;}
                else if (a == 4){ b = 2;}
                else if (a == 3){ b = 3;}
                if(b!=0){
                    Track.list.add(new Track(MainActivity.selectedLayer.getId(),b ,true));}
                MainActivity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                currentText = MainActivity.selectedLayer.getTextView().getText().toString();
                MainActivity.selectedLayer.getTextView().setText(currentText);
                break;

            case formatRight:
                // Set text alignment to the right
                a =  MainActivity.selectedLayer.getTextView().getTextAlignment();
                if (a == 2 || a ==1){ b = 1;}
                else if (a == 4){ b = 2;}
                else if (a == 3){ b = 0;}
                if(b!=0){
                    Track.list.add(new Track(MainActivity.selectedLayer.getId(),b ,true));}
                MainActivity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                currentText = MainActivity.selectedLayer.getTextView().getText().toString();
                MainActivity.selectedLayer.getTextView().setText(currentText);

                break;
            case formatItalic:
                // Set text style to italic
                if (currentTypeface != null) {
                    if ((currentTypeface.getStyle() == Typeface.BOLD_ITALIC)) {
                        MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD);
                    } else if ((currentTypeface.getStyle() == Typeface.ITALIC)) {
                        MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.NORMAL);
                    } else if ((currentTypeface.getStyle() == Typeface.BOLD)) {
                        MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD_ITALIC);
                    }

                } else {
                    MainActivity.selectedLayer.getTextView().setTypeface(null, Typeface.ITALIC);
                }
                break;
            case formatCenter:
                // Set text alignment to center
                a =  MainActivity.selectedLayer.getTextView().getTextAlignment();
                if (a == 2 || a ==1){ b = 1;}
                else if (a == 4){ b = 0;}
                else if (a == 3){ b = 3;}
                if(b!=0){
                Track.list.add(new Track(MainActivity.selectedLayer.getId(),b ,true));}
                MainActivity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                currentText = MainActivity.selectedLayer.getTextView().getText().toString();
                MainActivity.selectedLayer.getTextView().setText(currentText);
                break;
            case Format:
                String originalText = MainActivity.selectedLayer.getTextView().getText().toString();
                Track.list.add(new Track(true,MainActivity.selectedLayer.getId(),MainActivity.selectedLayer.getTextView().getText().toString(),-1));
                Track.list2.clear();

                if (originalText.equals(originalText.toLowerCase())) {
                    // Convert text to uppercase
                    String UpperCaseText = originalText.toUpperCase();
                    MainActivity.selectedLayer.getTextView().setText(UpperCaseText);
                } else {
                    // Convert text to lowercase
                    String lowerCaseText = originalText.toLowerCase();
                    MainActivity.selectedLayer.getTextView().setText(lowerCaseText);
                }

                break;








        }

    }

    public static void applyHueFilter(Context context, ImageView imageView, float hue) {
        // Get the drawable from the ImageView
        Drawable drawable = imageView.getDrawable();

        // Apply hue filter
        if (drawable != null) {
            // Create a color matrix with the desired hue adjustment
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setRotate(0, hue); // Red
            colorMatrix.setRotate(1, hue); // Green
            colorMatrix.setRotate(2, hue); // Blue

            // Apply the color matrix to a color filter
            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

            // Set the color filter to the drawable
            drawable.setColorFilter(colorFilter);

            // Update the ImageView
            imageView.setImageDrawable(drawable);
        }
    }

    public void onHueItemClick(float hueValue) {
        applyHueFilter(getContext(), MainActivity.selectedLayer1.getImageView(), hueValue);
    }
    public void huelistUpdate(){
        hueItemList.clear();
        if(MainActivity.selectedLayer1.getImageView()!= null){
        hueItemList.add(new HueItem(-150, MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(-120,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(-90,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(-60,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(-30,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(0,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(30,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(60,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(90,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(120,MainActivity.selectedLayer1.getImageView()));
        hueItemList.add(new HueItem(130,MainActivity.selectedLayer1.getImageView()));
    }}
}