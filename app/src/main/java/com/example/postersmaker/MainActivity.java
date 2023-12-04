package com.example.postersmaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
/** @noinspection deprecation*/
public class MainActivity extends AppCompatActivity implements CustomAdapter.OnItemSelected {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private final CustomAdapter customAdapter = new CustomAdapter(this);
    RecyclerView LayerRecycleView;
    List<String> textList = new ArrayList<>();
    Layers_Adapter adapter = new Layers_Adapter(this, textList, LayerRecycleView);
    public final List<FrameLayout> textLayoutList = new ArrayList<>();
    public static List<TextLayout> textLayoutList2 = new ArrayList<>();
    RelativeLayout borderLayout;
    TranslateAnimation fadeIn, fadeOut;
//    private final List<CustomAction> actions = new ArrayList<>();
    public TextLayout selectedLayer;
    Boolean isLocked;
    TextView textView;

    Button deleteButton, rotateButton, resizeButton, saveButton, LayerButton;
    HomeFragment homeFragment;
//    private int currentActionIndex = -1;
    public ImageView imageView, imgUndo, imgRedo;
    FrameLayout frameLayout, container;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.previewImageView);
        imageView.setImageResource(R.drawable.blank);
        container = findViewById(R.id.fragment_container);
        LayerRecycleView = findViewById(R.id.LayerRecycleView);
        LayerRecycleView.setVisibility(View.GONE);
        LayerButton = findViewById(R.id.LayerButton);
        RecyclerView recyclerView = findViewById(R.id.rvConstraintTools);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        frameLayout = new FrameLayout(this);
        adapter = new Layers_Adapter(MainActivity.this, textList, LayerRecycleView);


        LayerRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        adapter.textList.addAll(TextHandlerClass.getTextList());
        adapter.notifyDataSetChanged();
        LayerRecycleView.setAdapter(adapter);

        imgUndo = findViewById(R.id.imgUndo);
        imgRedo = findViewById(R.id.imgRedo);
//        imgUndo.setOnClickListener(v -> undo());
//
//        imgRedo.setOnClickListener(v -> redo());

        LayerButton.setOnClickListener(v -> {

            if (LayerRecycleView.getVisibility() == View.VISIBLE) {
                LayerRecycleView.setVisibility(View.GONE);
            } else {
                LayerRecycleView.setVisibility(View.VISIBLE);
            }

        });


    }

    @SuppressLint("ClickableViewAccessibility")
    TextLayout createTextLayout(String text, float x, float y) {
        frameLayout = new FrameLayout(this);
        // Create a FrameLayout to hold the TextView and the button
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setBackgroundResource(R.drawable.border_style);

        frameLayout.setMinimumWidth(20);
        TextLayout textLayout = new TextLayout(frameLayout, borderLayout, deleteButton, rotateButton, resizeButton, saveButton, textView, isLocked);
        textLayout.setFrameLayout(frameLayout);
        textLayout.setLocked(false);
        frameLayout.setTag(textLayout);
        selectedLayer = textLayout;

        textLayoutList2.add(textLayout);


        borderLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams borderLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        borderLayout.setLayoutParams(borderLayoutParams);
        int layoutMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 19, getResources().getDisplayMetrics());
        borderLayoutParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
        borderLayout.setGravity(Gravity.CENTER);
        textLayout.setBorderLayout(borderLayout);
        fadeIn = new TranslateAnimation(0, 0, 400, 0);
        fadeIn.setDuration(400);
        fadeOut = new TranslateAnimation(0, 0, 0, 400);
        fadeOut.setDuration(400);


        textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        borderLayout.setMinimumHeight(textView.getHeight() + 20);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setMaxWidth(imageView.getWidth() - 40);
        frameLayout.setMinimumHeight(textView.getHeight() + 20);

        deleteButton = new Button(this);
        textLayout.setDeleteButton(deleteButton);
        deleteButton.setBackgroundResource(R.drawable.close);
        deleteButton.setScaleX(0.26f);
        deleteButton.setScaleY(0.26f);
        FrameLayout.LayoutParams deleteButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteButtonParams.gravity = Gravity.TOP | Gravity.END;
        deleteButtonParams.setMargins(0, pxTodp(-29), pxTodp(-33), 0);
        deleteButton.setLayoutParams(deleteButtonParams);

        textLayout.getDeleteButton().setOnClickListener(v -> {
            ViewGroup viewGroup = findViewById(android.R.id.content);
            viewGroup.removeView(textLayout.getFrameLayout());
            textLayoutList.remove(textLayout.getFrameLayout());

            // Find the index of the TextLayout in textLayoutList2
            int index = textLayoutList2.indexOf(textLayout);

            if (index != -1) {
                TextHandlerClass.textList.remove(index);
                textLayoutList2.remove(index);
            }

            textList.remove(text);
            adapter.updateData(new ArrayList<>());
            adapter.textList.addAll(TextHandlerClass.getTextList());

            selectedLayer = null;
            LayerRecycleView.setVisibility(View.GONE);

            if (container.getVisibility() == View.VISIBLE) {
                container.setVisibility(View.GONE);
                container.startAnimation(fadeOut);
            }
        });
        rotateButton = new Button(this);
        rotateButton.setBackgroundResource(R.drawable.rotate);
        rotateButton.setScaleX(0.26f);
        rotateButton.setScaleY(0.26f);
        textLayout.setRotateButton(rotateButton);
        FrameLayout.LayoutParams rotateButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rotateButtonParams.setMargins(pxTodp(-33), pxTodp(-29), 0, 0);
        rotateButtonParams.gravity = Gravity.TOP | Gravity.START;
        rotateButton.setLayoutParams(rotateButtonParams);

        textLayout.getRotateButton().setOnTouchListener(new View.OnTouchListener() {
            private double startAngle;
            final float rotationSpeed = 0.0238f;
            private float currentRotation = 0f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        selectLayer(textLayout);
                        callSetDefaultState();
                        // Store the initial rotation angle
                        startAngle = getAngle((event.getX() / 10), (event.getY() / 10), textLayout.getFrameLayout().getPivotX(), textLayout.getFrameLayout().getPivotY());
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        double currentAngle = getAngle((event.getX() / 10), (event.getY() / 10), textLayout.getFrameLayout().getPivotX(), textLayout.getFrameLayout().getPivotY());

                        // Calculate the angle difference and apply the rotation speed factor
                        float newRotation = (float) (Math.toDegrees(currentAngle - startAngle) * rotationSpeed);
                        currentRotation += newRotation;

                        // Apply the new rotation to the FrameLayout
                        textLayout.getFrameLayout().setRotation(currentRotation);
                        return true;
                }
                return true;
            }
        });
        // Add the TextView to the border layout
        borderLayout.addView(textView);
        // Create a button to resize the text
        resizeButton = new Button(this);
        textLayout.setResizeButton(resizeButton);
        resizeButton.setBackgroundResource(R.drawable.resize);
        resizeButton.setScaleX(0.26f);
        resizeButton.setScaleY(0.26f);
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.BOTTOM | Gravity.END;
        buttonParams.setMargins(0, 0, pxTodp(-31), pxTodp(-29));
        resizeButton.setLayoutParams(buttonParams);
        textLayout.getResizeButton().setOnTouchListener(new View.OnTouchListener() {
            private float lastX = 0f, lastY = 0f;
            final int MAX_TEXT_SIZE = pxTodp(120);

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        selectLayer(textLayout);
                        if (lastX == 0f && lastY == 0f) {
                            lastX = textLayout.getFrameLayout().getX();
                            lastY = textLayout.getFrameLayout().getY();
                         }
                        else {
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                         }

                         callSetDefaultState();
                         break;

                    case MotionEvent.ACTION_UP:


                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX();
                        float newY = event.getRawY();

                        // Calculate the direction of resizing based on the current rotation angle
                        float currentRotation = textLayout.getFrameLayout().getRotation();
                        double angleInRadians = Math.toRadians(currentRotation);
                        float cosTheta = (float) Math.cos(angleInRadians);
                        float sinTheta = (float) Math.sin(angleInRadians);

                        // Calculate the relative movement in the rotated coordinates
                        float dx = (newX - lastX) * cosTheta + (newY - lastY) * sinTheta;
                        float dy = -(newX - lastX) * sinTheta + (newY - lastY) * cosTheta;

                        // Apply resizing along the layout's axes in the rotated coordinates
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textLayout.getBorderLayout().getLayoutParams();
                        int currentWidth = params.width;
                        int currentHeight = params.height;

                        // Check for minimum and maximum dimensions
                        int minWidth = pxTodp(40); // Minimum width
                        int minHeight = textLayout.getTextView().getHeight(); // Minimum height
                        int maxWidth = textLayout.getFrameLayout().getWidth() - pxTodp(32);
                        int maxHeight = imageView.getHeight();

                        if (currentWidth + dx < minWidth) {
                            params.width = minWidth;
                        } else if (currentWidth + dx > maxWidth) {
                            params.width = maxWidth;
                        } else {
                            params.width += dx * 2;
                        }

                        // Calculate the number of lines in the text
                        int textHeight = textLayout.getTextView().getLineCount() * textLayout.getTextView().getLineHeight();

                        // If the width is less than the text width, increase the height to accommodate the text
                        if (params.width < textLayout.getTextView().getWidth()) {
                            params.height = Math.max(textHeight, textLayout.getTextView().getHeight());
                        } else {
                            if (currentHeight + dy < minHeight) {
                                params.height = minHeight;
                            } else if (currentHeight + dy > maxHeight) {
                                params.height = maxHeight;
                            } else {
                                params.height += dy;
                            }
                        }
                        // Check for maximum height
                        if (params.height > maxHeight) {
                            params.height = maxHeight;
                        }
                        // Apply the new dimensions to the border layout


                        // Adjust the text size based on the height and width limits
                        float textSize = textLayout.getTextView().getTextSize();
                        float newSize = textSize;
                        if (dy > 0) {
                            if (params.height > textHeight && textLayout.getTextView().getWidth() < params.width - pxTodp(24) && textLayout.getFrameLayout().getWidth() < imageView.getWidth() - pxTodp(24)) {
                                newSize = textSize + dy / 4.3f;
                            }
                        } else if (dy < 0) {
                            if (textLayout.getFrameLayout().getWidth() < imageView.getWidth() - pxTodp(24)) {
                                newSize = textSize + dy / 4.3f;
                            }
                        }

                        textLayout.getBorderLayout().setLayoutParams(params);


                        // Check for maximum text size
                        if (newSize > MAX_TEXT_SIZE) {
                            newSize = MAX_TEXT_SIZE;
                        }
                        if (newSize < pxTodp(19)) {
                            newSize = pxTodp(19);
                        }
                        // Adjust text size based on the height and width limits
                        float maxWidthBasedSize = Math.min(params.width, params.height);
                        if (newSize > maxWidthBasedSize) {
                            newSize = maxWidthBasedSize;
                        }
                        textLayout.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
                        lastX = newX;
                        lastY = newY;
                        params.height = textLayout.getTextView().getHeight() + textLayout.getTextView().getLineHeight();
                        textLayout.getTextView().setMaxWidth(params.width - pxTodp(24));

                        break;
                }
                return true;
            }
        });
        saveButton = new Button(this);
        textLayout.setSaveButton(saveButton);
        saveButton.setBackgroundResource(R.drawable.checked);
        saveButton.setScaleX(0.282f);
        saveButton.setScaleY(0.282f);
        FrameLayout.LayoutParams saveButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        saveButtonParams.setMargins(pxTodp(-30), 0, 0, pxTodp(-18));
        saveButtonParams.gravity = Gravity.BOTTOM | Gravity.START;
        saveButton.setLayoutParams(saveButtonParams);
        if (selectedLayer != null && textLayout.getFrameLayout() != null) {
            textLayout.getSaveButton().setOnClickListener(v -> {
                unselectLayer(selectedLayer);
                selectedLayer = null;
                callSetDefaultState();
                if (container.getVisibility() == View.VISIBLE) {
                    container.setVisibility(View.GONE);
                    container.startAnimation(fadeOut);
                }


            });
        }
        imageView.setOnClickListener(v -> {
            unselectLayer(selectedLayer);
            selectedLayer = null;
            LayerRecycleView.setVisibility(View.GONE);
            callSetDefaultState();
            if (container.getVisibility() == View.VISIBLE) {
                container.setVisibility(View.GONE);
                container.startAnimation(fadeOut);
            }
        });
        if (selectedLayer != null && textLayout.getFrameLayout() != null) {
            textLayout.setTextView(textView);

            textLayout.getTextView().setOnTouchListener(new View.OnTouchListener() {
                private float lastX, lastY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                            unselectLayer(selectedLayer);
                            selectLayer(textLayout);

                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if (!textLayout.getLocked()) {
                                float newX = event.getRawX();
                                float newY = event.getRawY();
                                float dX = newX - lastX;
                                float dY = newY - lastY;

                                // Update the position of the frameLayout based on the drag movement
                                textLayout.getFrameLayout().setX(textLayout.getFrameLayout().getX() + dX);
                                textLayout.getFrameLayout().setY(textLayout.getFrameLayout().getY() + dY);

                                lastX = newX;
                                lastY = newY;
                                break;
                            }
                    }
                    return true;
                }
            });

            textLayout.getFrameLayout().setOnTouchListener(new View.OnTouchListener() {
                private float lastX, lastY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = event.getRawX();
                            lastY = event.getRawY();
                            unselectLayer(selectedLayer);
                            selectLayer(textLayout);

                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if (!textLayout.getLocked()) {
                                float newX = event.getRawX();
                                float newY = event.getRawY();
                                float dX = newX - lastX;
                                float dY = newY - lastY;

                                textLayout.getFrameLayout().setX(textLayout.getFrameLayout().getX() + dX);
                                textLayout.getFrameLayout().setY(textLayout.getFrameLayout().getY() + dY);

                                lastX = newX;
                                lastY = newY;
                                break;
                            }
                    }
                    return true;
                }
            });
        }
        textLayout.getBorderLayout().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                unselectLayer(selectedLayer);
                selectLayer(textLayout);
            }
            return false;
        });
        textLayout.getFrameLayout().addView(textLayout.getBorderLayout());
        textLayout.getFrameLayout().addView(textLayout.getResizeButton());
        textLayout.getFrameLayout().addView(textLayout.getDeleteButton());
        textLayout.getFrameLayout().addView(textLayout.getRotateButton());
        textLayout.getFrameLayout().addView(textLayout.getSaveButton());
        textLayout.getFrameLayout().setX(x);
        textLayout.getFrameLayout().setY(y);

        return textLayout;
    }


    public void selectLayer(TextLayout textLayout) {
        unselectLayer(selectedLayer);
        if (!textLayout.getLocked()) {

                FrameLayout layer = textLayout.getFrameLayout();
                if (layer != null) {
                    Button resizeButton = textLayout.getResizeButton();
                    Button deleteButton = textLayout.getDeleteButton();
                    Button rotateButton = textLayout.getRotateButton();
                    Button saveButton = textLayout.getSaveButton();

                    // Make the associated buttons and features visible
                    if (resizeButton != null && deleteButton != null && rotateButton != null && saveButton != null) {
                        resizeButton.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        rotateButton.setVisibility(View.VISIBLE);
                        saveButton.setVisibility(View.VISIBLE);
                    }
                    if (container.getVisibility() == View.GONE || container.getVisibility() == View.INVISIBLE) {
                        container.setVisibility(View.VISIBLE);
                        // Animation duration in milliseconds
                        container.startAnimation(fadeIn);
                    }
                    // Set the background resource to indicate selection
                    layer.setBackgroundResource(R.drawable.border_style);
                }
                selectedLayer = textLayout;

        }
    }

    public void unselectLayer(TextLayout textLayout) {
        if (textLayout != null) {
            FrameLayout layer = textLayout.getFrameLayout();
            if (layer != null) {
                Button resizeButton = textLayout.getResizeButton();
                Button deleteButton = textLayout.getDeleteButton();
                Button rotateButton = textLayout.getRotateButton();
                Button saveButton = textLayout.getSaveButton();
                if (resizeButton != null && deleteButton != null && rotateButton != null && saveButton != null) {
                    resizeButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    rotateButton.setVisibility(View.INVISIBLE);
                    saveButton.setVisibility(View.INVISIBLE);
                }

                callSetDefaultState();
                // Set the background resource to indicate selection
                layer.setBackground(null);
            }
        }
    }

//    void addAction(CustomAction action) {
//        if (currentActionIndex < actions.size() - 1) {
//            actions.subList(currentActionIndex + 1, actions.size()).clear();
//        }
//        actions.add(action);
//        currentActionIndex = actions.size() - 1;
//    }

//    private void undo() {
//        if (currentActionIndex >= 0) {
//            CustomAction action = actions.get(currentActionIndex);
//            action.undo();
//            currentActionIndex--;
//        }
//    }
//
//    private void redo() {
//        if (currentActionIndex < actions.size() - 1) {
//            currentActionIndex++;
//            CustomAction action = actions.get(currentActionIndex);
//            action.redo();
//        }
//    }



//    static class CustomAction {
//        private final Runnable undoAction;
//        private final Runnable redoAction;
//
//        public CustomAction(Runnable undoAction, Runnable redoAction) {
//            this.undoAction = undoAction;
//            this.redoAction = redoAction;
//        }
//
//        public void undo() {
//            undoAction.run();
//        }
//
//        public void redo() {
//            redoAction.run();
//        }
//    }

    private double getAngle(double x, double y, float pivotX, float pivotY) {
        double rad = Math.atan2(y - pivotY, x - pivotX) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }

//    private boolean isViewInBounds(View view, int x, int y) {
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        int viewX = location[0];
//        int viewY = location[1];
//        int viewWidth = view.getWidth();
//        int viewHeight = view.getHeight();
//        return (x >= viewX && x <= (viewX + viewWidth)) && (y >= viewY && y <= (viewY + viewHeight));
//    }

    @Override
    public void onToolSelected(ToolTypeForCustomAdaptor toolType) {
        switch (toolType) {
            case TEXT:
                TextHandlerClass.showTextDialog(this, textLayoutList, findViewById(android.R.id.content));
                break;
            case Photo:
                startActivityForResult(new Intent(this, Image_pick.class), REQUEST_CODE_PICK_IMAGE);
                break;
            case FILTER:
            case EMOJI:
                // Implement as needed
                break;
        }
    }


    public void onEditButtonClick(int index) {


        // Find the TextLayout in textLayoutList based on the text
        TextLayout selectedTextLayout = findTextLayoutByText(index);

        // Call the selectLayer method in your MainActivity
        if (selectedTextLayout != null) {
            selectLayer(selectedTextLayout);
        }
    }

    public void onLockButtonClick(int index) {
        TextLayout selectedTextLayout = findTextLayoutByText(index);
        if (selectedTextLayout != null) {
            if (selectedTextLayout.getLocked()) {
                selectedTextLayout.setLocked(false);

            } else {
                selectedTextLayout.setLocked(true);
                if (selectedLayer == selectedTextLayout) {
                    unselectLayer(selectedTextLayout);
                }
            }
            adapter.selectedTextLayout = selectedTextLayout;
        }
    }

    public TextLayout findTextLayoutByText(int index) {

        for (TextLayout textLayout : textLayoutList2) {

            if (index == textLayoutList2.indexOf(textLayout)) {
                return textLayout;
            }
        }

        return null;
    }


    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public int pxTodp(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    public void callSetDefaultState() {
        if (homeFragment != null) {
            homeFragment.setDefaultStateFromExternal();
        }
    }

}