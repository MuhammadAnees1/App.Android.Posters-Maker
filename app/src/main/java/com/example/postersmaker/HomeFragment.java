package com.example.postersmaker;

import static android.app.ProgressDialog.show;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements EditTextAdapter.OnItemSelected, TypeTextAdapter.onToolSelecteds {
    static TranslateAnimation fadeIn;
    TextLayout selectedLayer;
    ImageLayout selectedLayer1;
    MainActivity activity;
    FrameLayout frameLayout,fragmentContainer1, fontContainer;
    List<Integer> colors = getYourColorList();
    float lastSetTextSize = 1f;
    float initialTextSize;
    ColorPickerFragment colorPickerFragment;
    TextView lineStrokeButton, dashStrokeButton, dotStrokeButton;
    SeekBar seekBar;
    Handler handler;
    TextView buttonApplyFont;
    String currentText;
    LinearLayout text_buttonsUp,StrokeLayout;
    private StrokeType currentStrokeType = StrokeType.LINE;
    static RecyclerView recyclerView;
    RecyclerView TypeTextLayout;
    Button UpButton, downButton, leftButton, rightButton ,editButton,flipButton ;
    static Button Image_control_button,Image_control_opacity;
    Paint textPaint;
    private Paint strokePaint;
    private final EditTextAdapter editTextAdapter = new EditTextAdapter(this);
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
        recyclerView = view.findViewById(R.id.editTextLayout);
        TypeTextLayout = view.findViewById(R.id.TypeTextLayout);
        StrokeLayout = view.findViewById(R.id.StrokeLayout);
        lineStrokeButton = view.findViewById(R.id.LineStroke);
        dashStrokeButton = view.findViewById(R.id.DashStroke);
        dotStrokeButton = view.findViewById(R.id.DotStroke);
        fragmentContainer1 = view.findViewById(R.id.fragment_container1);
        fontContainer = view.findViewById(R.id.font_container);
        Image_control_button= view.findViewById(R.id.Image_control_button);
        Image_control_opacity= view.findViewById(R.id.Image_control_opacity);
        flipButton = view.findViewById(R.id.FlipButton);

        Bundle arguments = getArguments();
        if (arguments != null) {
            openedFromImagePickerManager = arguments.getBoolean("openedFromImagePickerManager", true);
        }
        if (openedFromImagePickerManager) {
            setDefaultState();
            Image_control_button.setVisibility(View.VISIBLE);
            Image_control_opacity.setVisibility(View.VISIBLE);
            flipButton.setVisibility(View.INVISIBLE);
            }
        else {
            if(recyclerView.getVisibility() != View.VISIBLE) {
                recyclerView.setVisibility(View.VISIBLE);
                flipButton.setVisibility(View.INVISIBLE);
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
                seekBar.setVisibility(View.VISIBLE);

        });
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLayer1 = MainActivity.selectedLayer1;
                    // Get the current scaleX value of the ImageView
                    float currentScaleX = selectedLayer1.getImageView().getScaleX();
                    // Flip the image horizontally by changing the scaleX value
                    selectedLayer1.getImageView().setScaleX(-currentScaleX);
                    // You can also use the following line to flip the image vertically
                    // imageView.setScaleY(-imageView.getScaleY());
                    // Show a toast message to indicate that the image has been flipped
                    Toast.makeText(activity, "Image flipped", Toast.LENGTH_SHORT).show();
            }

        });
        Image_control_button.setOnClickListener(v -> {
            if(text_buttonsUp.getVisibility() != View.VISIBLE){
                setDefaultState();
                text_buttonsUp.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                flipButton.setVisibility(View.VISIBLE);
//                    text_buttonsUp.startAnimation(activity.fadeIn);

            }
        });
        seekBar.setProgress(100); // Set the initial progress to 100
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }
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
                        moveFrameLayoutUpContinuously();
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
                        moveFrameLayoutUpContinuously();
                    }
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
                        moveFrameLayoutDownContinuously();
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
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
                        moveFrameLayoutLeftContinuously();
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
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
                        moveFrameLayoutRightContinuously();
                    }
                    else {
                        selectedLayer1 = MainActivity.selectedLayer1;
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
    private void onStrokeTypeSelected(StrokeType strokeType) {
        currentStrokeType = strokeType;
        if (MainActivity.selectedLayer != null) {
            MainActivity.selectedLayer.setStrokeType(strokeType);
        }
    }
    private void setDefaultState() {
        // Set your default UI state here
        fontContainer.setVisibility(View.GONE);
        fragmentContainer1.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        Image_control_button.setVisibility(View.GONE);
        Image_control_opacity.setVisibility(View.GONE);
        TypeTextLayout.setVisibility(View.GONE);
        text_buttonsUp.setVisibility(View.GONE);
        StrokeLayout.setVisibility(View.GONE);
    }
    @Override
    public void onToolSelected(ToolTypesForEditAdaptor toolType) {
        switch (toolType) {
            case Control:
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
                if(seekBar.getVisibility() != View.VISIBLE){
                    setDefaultState();
                    seekBar.setVisibility(View.VISIBLE);
                    seekBar.startAnimation(MainActivity.fadeIn);}

                // Set minimum and maximum text size
                float minTextSize = 10.0f;
                float maxTextSize = activity.pxTodp(120);

                // Set initial text size
                selectedLayer = MainActivity.selectedLayer;
                if(selectedLayer != null) {
                    initialTextSize = selectedLayer.getTextView().getTextSize();
                    lastSetTextSize = initialTextSize;

                    // Set the maximum and minimum values for the SeekBar
                    seekBar.setMax((int) (maxTextSize - minTextSize));
                    seekBar.setProgress((int) (lastSetTextSize - minTextSize));

                }
                // Add a listener to the SeekBar to adjust the text size in real-time
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Calculate the current text size based on progress

                        float textSize = minTextSize + progress;

                        // Ensure that the text size does not exceed the maximum limit
                        textSize = Math.max(minTextSize, Math.min(textSize, maxTextSize));

                        textSize = Math.min(textSize, initialTextSize);
                        // Set the text size
                        activity.selectedLayer.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        // Notify the parent view to request a layout pass
                        selectedLayer.getFrameLayout().requestLayout();

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Handle touch event start if needed
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Handle touch event stop if needed
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
                        float limitedProgress = Math.min(Math.max(progress, 0), 100);

                        // Map the progress value to the desired shadow range
                        float shadowValue = minShadow + (maxShadow - minShadow) * (limitedProgress / 100.0f);

                        // Apply the shadow to the selectedLayer's TextView
                        MainActivity.selectedLayer.getTextView().setShadowLayer(shadowValue, shadowValue, shadowValue, Color.BLACK);
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
            case Space:
                if (seekBar.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    seekBar.setVisibility(View.VISIBLE);
                    seekBar.startAnimation(activity.fadeIn);
                }
                seekBar.setMax(activity.pxTodp(33));
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float letterSpacing = progress / (float)activity.pxTodp(33);
                        if (activity.selectedLayer != null) {
                            activity.selectedLayer.getTextView().setLetterSpacing(letterSpacing);
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Handle touch event start if needed
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Handle touch event stop if needed
                    }
                });
                break;
            case stroke:
                lineStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.LINE));
                dashStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.DASH));
                dotStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.DOT));

                if (StrokeLayout.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    StrokeLayout.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    StrokeLayout.startAnimation(activity.fadeIn);
                }

                final float minStrokeWidth = 1.0f;
                final float maxStrokeWidth = 10.0f;

                final Paint strokePaint = activity.selectedLayer.getTextView().getPaint();
                strokePaint.setStyle(Paint.Style.STROKE);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float limitedProgress = Math.min(Math.max(progress, 0), activity.pxTodp(40));
                        float strokeWidth = minStrokeWidth + (maxStrokeWidth - minStrokeWidth) * (limitedProgress / (float) activity.pxTodp(40));
                        strokePaint.setStrokeWidth(strokeWidth);
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
        Typeface currentTypeface = activity.selectedLayer.getTextView().getTypeface();


        switch (toolType) {
            case formatBold:
                // Toggle bold
                if (currentTypeface != null) {
                    if ((currentTypeface.getStyle() == Typeface.BOLD_ITALIC)) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.ITALIC);
                    } else if ((currentTypeface.getStyle() == Typeface.BOLD)) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.NORMAL);
                    } else if ((currentTypeface.getStyle() == Typeface.ITALIC)) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD_ITALIC);
                    }

                }
                else {
                    activity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD);
                }
                break;
            case FormatUnderlined:
                // Underline the text
                if ((MainActivity.selectedLayer.getTextView().getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) != 0) {
                    MainActivity.selectedLayer.getTextView().setPaintFlags(MainActivity.selectedLayer.getTextView().getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                } else {
                    activity.selectedLayer.getTextView().setPaintFlags(MainActivity.selectedLayer.getTextView().getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                break;
            case formatLeft:
                // Set text alignment to the left
                activity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                currentText = activity.selectedLayer.getTextView().getText().toString();
                activity.selectedLayer.getTextView().setText(currentText);
                break;

            case formatRight:
                // Set text alignment to the right
                activity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                currentText = activity.selectedLayer.getTextView().getText().toString();
                activity.selectedLayer.getTextView().setText(currentText);

                break;
            case formatItalic:
                // Set text style to italic
                if (currentTypeface != null) {
                    if ((currentTypeface.getStyle() == Typeface.BOLD_ITALIC)) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD);
                    } else if ((currentTypeface.getStyle() == Typeface.ITALIC)) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.NORMAL);
                    } else if ((currentTypeface.getStyle() == Typeface.BOLD)) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD_ITALIC);
                    }

                } else {
                    activity.selectedLayer.getTextView().setTypeface(null, Typeface.ITALIC);
                }
                break;
            case formatCenter:
                // Set text alignment to center
                activity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                currentText = activity.selectedLayer.getTextView().getText().toString();
                activity.selectedLayer.getTextView().setText(currentText);
                break;
            case Format:
                String originalText = activity.selectedLayer.getTextView().getText().toString();
                if (originalText.equals(originalText.toLowerCase())) {
                    // Convert text to uppercase
                    String UpperCaseText = originalText.toUpperCase();
                    activity.selectedLayer.getTextView().setText(UpperCaseText);
                } else {
                    // Convert text to lowercase
                    String lowerCaseText = originalText.toLowerCase();
                    activity.selectedLayer.getTextView().setText(lowerCaseText);
                }
                break;
        }

    }

}