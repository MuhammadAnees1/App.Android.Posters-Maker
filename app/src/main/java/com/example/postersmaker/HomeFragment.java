package com.example.postersmaker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    float lastSetTextSize = 1f;
    float initialTextSize;
    TextView lineStrokeButton, dashStrokeButton, dotStrokeButton;
    SeekBar seekBar;
    Handler handler;
    TextView buttonApplyFont;
    RelativeLayout text_buttonsUp , FontsLayout;
    LinearLayout StrokeLayout;
    private StrokeType currentStrokeType = StrokeType.LINE;
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
        StrokeLayout = view.findViewById(R.id.StrokeLayout);
        lineStrokeButton = view.findViewById(R.id.LineStroke);
        dashStrokeButton = view.findViewById(R.id.DashStroke);
        dotStrokeButton = view.findViewById(R.id.DotStroke);


        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setHomeFragment(this);
        }

        handler = new Handler();
        activity = (MainActivity) requireActivity();
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

        lineStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.LINE));
        dashStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.DASH));
        dotStrokeButton.setOnClickListener(v -> onStrokeTypeSelected(StrokeType.DOT));
        return view;
    }

    private void onStrokeTypeSelected(StrokeType strokeType) {
        currentStrokeType = strokeType;
        if (activity.selectedLayer != null) {
            activity.selectedLayer.setStrokeType(strokeType);
        }
    }

    private void setDefaultState() {
        // Set your default UI state here
        FontsLayout.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
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
                    text_buttonsUp.startAnimation(activity.fadeIn);}
                break;
            case Style:
                if(TypeTextLayout.getVisibility() != View.VISIBLE){
                    setDefaultState();
                    TypeTextLayout.setVisibility(View.VISIBLE);
                    TypeTextLayout.startAnimation(activity.fadeIn);}

                break;
            case text_size:
                if(seekBar.getVisibility() != View.VISIBLE){
                    setDefaultState();
                    seekBar.setVisibility(View.VISIBLE);
                    seekBar.startAnimation(activity.fadeIn);}

                // Set minimum and maximum text size
                float minTextSize = 10.0f;
                float maxTextSize = 300.0f;

                // Set initial text size
                selectedLayer = activity.selectedLayer;
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
                if (FontsLayout.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    FontsLayout.setVisibility(View.VISIBLE);
                    FontsLayout.startAnimation(activity.fadeIn);
                }
                buttonApplyFont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Typeface customFont1 = ResourcesCompat.getFont(activity, R.font.abril_fatface);
                        selectedLayer.getTextView().setTypeface(customFont1);
                    }
                });
                break;
            case Shadow:
                if (seekBar.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    seekBar.setVisibility(View.VISIBLE);
                    seekBar.startAnimation(activity.fadeIn);}
                final float minShadow = 0.0f; // Set your minimum shadow value
                final float maxShadow = 20.0f; // Set your maximum shadow value
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Constrain the progress within the desired range
                        float limitedProgress = Math.min(Math.max(progress, 0), 100);

                        // Map the progress value to the desired shadow range
                        float shadowValue = minShadow + (maxShadow - minShadow) * (limitedProgress / 100.0f);

                        // Apply the shadow to the selectedLayer's TextView
                        activity.selectedLayer.getTextView().setShadowLayer(shadowValue, shadowValue, shadowValue, Color.BLACK);
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
            case stroke:
                if (StrokeLayout.getVisibility() != View.VISIBLE) {
                    setDefaultState();
                    StrokeLayout.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    StrokeLayout.startAnimation(activity.fadeIn);
                }

                // Set your minimum and maximum stroke width
                final float minStrokeWidth = 1.0f;
                final float maxStrokeWidth = 10.0f;

                // Create a Paint object for stroke
                final Paint strokePaint = activity.selectedLayer.getTextView().getPaint();
                strokePaint.setStyle(Paint.Style.STROKE);


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Constrain the progress within the desired range
                        float limitedProgress = Math.min(Math.max(progress, 0), 100);

                        // Map the progress value to the desired stroke width range
                        float strokeWidth = minStrokeWidth + (maxStrokeWidth - minStrokeWidth) * (limitedProgress / 100.0f);

                        activity.selectedLayer.setStrokeType(currentStrokeType);
                        // Apply the stroke width to the selectedLayer's TextView
                        strokePaint.setStrokeWidth(strokeWidth);
                        strokePaint.setColor(Color.BLACK);

                        // Redraw the TextView to reflect the changes
                        activity.selectedLayer.getTextView().invalidate();
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
                // Unselecting the text_size button, so set seekBar to GONE and text_buttonsUp to VISIBLE
                setDefaultState();
        }
    }
    void setDefaultStateFromExternal() {
        setDefaultState();
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
        Typeface currentTypeface = activity.selectedLayer.getTextView().getTypeface();

        switch (toolType) {
            case formatBold:
                // Toggle bold
                if (currentTypeface != null) {
                    if ((currentTypeface.getStyle() & Typeface.BOLD) != 0) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.NORMAL);
                    }
                } else {
                    activity.selectedLayer.getTextView().setTypeface(null, Typeface.BOLD);
                }
                break;
            case FormatUnderlined:
                // Underline the text
                if ((activity.selectedLayer.getTextView().getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) != 0) {
                    activity.selectedLayer.getTextView().setPaintFlags(activity.selectedLayer.getTextView().getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                } else {
                    activity.selectedLayer.getTextView().setPaintFlags(activity.selectedLayer.getTextView().getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                break;
            case formatLeft:
                // Set text alignment to the left
                activity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                break;

            case formatRight:
                // Set text alignment to the right
                activity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                break;
            case formatItalic:
                // Set text style to italic
                if (currentTypeface != null) {
                    if ((currentTypeface.getStyle() & Typeface.ITALIC) != 0) {
                        activity.selectedLayer.getTextView().setTypeface(null, Typeface.NORMAL);
                    }
                } else {
                    activity.selectedLayer.getTextView().setTypeface(null, Typeface.ITALIC);
                }
                break;
            case formatCenter:
                // Set text alignment to center
                activity.selectedLayer.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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