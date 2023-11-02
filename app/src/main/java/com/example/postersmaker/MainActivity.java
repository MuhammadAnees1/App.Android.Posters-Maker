package com.example.postersmaker;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
//nothing
public class MainActivity extends AppCompatActivity implements CustomAdapter.OnItemSelected {
    private final CustomAdapter customAdapter = new CustomAdapter(this);
    public final List<FrameLayout> textLayoutList = new ArrayList<>();
    float dX = 0, dY = 0;
    private final List<CustomAction> actions = new ArrayList<>();

    TextView textView;
    Button deleteButton,rotateButton,resizeButton,saveButton;
    private int currentActionIndex = -1;

    public FrameLayout selectedLayer = null;

    private ImageView imageView;
    private ImageView imgUndo;
    FrameLayout frameLayout;
    private ImageView imgRedo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.previewImageView);
        imageView.setImageResource(R.drawable.blank);

        RecyclerView recyclerView = findViewById(R.id.rvConstraintTools);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         frameLayout = new FrameLayout(this);

//        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
//        if (imageUri != null) {
//            imageView.setImageURI(imageUri);
//        }
        imgUndo = findViewById(R.id.imgUndo);
        imgRedo = findViewById(R.id.imgRedo);
        imgUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });

        imgRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redo();
            }
        });


    }
    @Override
    public void onToolSelected(ToolType toolType) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (toolType) {
            case TEXT:
                TextHandlerClass.showTextDialog(this, textLayoutList, (ViewGroup) findViewById(android.R.id.content));
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.fragment_container, homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case ERASER:
            case FILTER:
            case STICKER:
                // Implement as needed
                break;
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    TextLayout createTextLayout(String text, float x, float y) {
         frameLayout = new FrameLayout(this);
        // Create a FrameLayout to hold the TextView and the button
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setBackgroundResource(R.drawable.border_style);
        frameLayout.setMinimumHeight(20);
        frameLayout.setMinimumWidth(20);
        TextLayout textLayout = new TextLayout(frameLayout, deleteButton, rotateButton, resizeButton, saveButton, textView);
        textLayout.setFrameLayout(frameLayout);
        selectedLayer = textLayout.getFrameLayout();

















        RelativeLayout borderLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams borderLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        borderLayout.setLayoutParams(borderLayoutParams);
        borderLayoutParams.setMargins(67, 67, 97, 67);
//        borderLayout.setBackgroundColor(Color.parseColor("#b05c56"));
        borderLayout.setGravity(Gravity.CENTER);


//        borderLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                float x = event.getX();
//                float y = event.getY();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//
//                        borderLayout.setX(x - borderLayout.getWidth() / 2);
//                        borderLayout.setY(y - borderLayout.getHeight() / 2);
//                        break;}
//                return false;
//            }
//        });






        textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(Color.BLACK);
        borderLayout.setMinimumHeight(textView.getHeight()+20);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setMaxWidth(imageView.getWidth()-40);






        deleteButton = new Button(this);
        textLayout.setDeleteButton(deleteButton);
        deleteButton.setBackgroundResource(R.drawable.close);
        deleteButton.setScaleX(0.3f);
        deleteButton.setScaleY(0.3f);
        FrameLayout.LayoutParams deleteButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteButtonParams.gravity = Gravity.TOP | Gravity.END;
        deleteButtonParams.setMargins(0,-75,-85,0);
        deleteButton.setLayoutParams(deleteButtonParams);

        textLayout.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = findViewById(android.R.id.content);
                viewGroup.removeView(textLayout.getFrameLayout());
                textLayoutList.remove(textLayout.getFrameLayout());
            }
        });
         rotateButton = new Button(this);
        rotateButton.setBackgroundResource(R.drawable.rotate);
        rotateButton.setScaleX(0.3f);
        rotateButton.setScaleY(0.3f);
        textLayout.setRotateButton(rotateButton);
        FrameLayout.LayoutParams rotateButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rotateButtonParams.setMargins(-85,-75,0,0);
        rotateButtonParams.gravity = Gravity.TOP | Gravity.START;
        rotateButton.setLayoutParams(rotateButtonParams);

        textLayout.getRotateButton().setOnTouchListener(new View.OnTouchListener() {
            private double startAngle;
            float rotationSpeed = 0.0238f;
            private float currentRotation = 0f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Store the initial rotation angle
                        startAngle = getAngle((event.getX()/10), (event.getY()/10), textLayout.getFrameLayout().getPivotX(), textLayout.getFrameLayout().getPivotY());
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        double currentAngle = getAngle((event.getX()/10), (event.getY()/10), textLayout.getFrameLayout().getPivotX(), textLayout.getFrameLayout().getPivotY());

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




        // Create the TextView



        // Add a visible border around the text layer



        // Add the TextView to the border layout
        borderLayout.addView(textView);
        // Create a button to resize the text
       resizeButton = new Button(this);
        textLayout.setResizeButton(resizeButton);

        resizeButton.setBackgroundResource(R.drawable.resize);
        resizeButton.setScaleX(0.3f);
        resizeButton.setScaleY(0.3f);
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.BOTTOM | Gravity.END;
        buttonParams.setMargins(0,0,-85,-75);
        resizeButton.setLayoutParams(buttonParams);


        textLayout.getResizeButton().setOnTouchListener(new View.OnTouchListener() {
            private float lastX, lastY;
            private boolean isDragging = false;
            private static final int MAX_TEXT_SIZE = 300; // Set your maximum text size here

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
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
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) borderLayout.getLayoutParams();
                        int currentWidth = params.width;
                        int currentHeight = params.height;

                        // Check for minimum and maximum dimensions
                        int minWidth = 100; // Minimum width
                        int minHeight = textLayout.getTextView().getHeight(); // Minimum height
                        int maxWidth = imageView.getWidth() - 10; // 10 less than imageView width
                        int maxHeight = imageView.getHeight();

                        if (currentWidth + dx < minWidth) {
                            params.width = minWidth;
                        } else if (currentWidth + dx > maxWidth) {
                            params.width = maxWidth;
                        } else {
                            params.width += dx;
                        }

                        // Calculate the number of lines in the text
                        int textHeight = textLayout.getTextView().getLineCount() * textLayout.getTextView().getLineHeight();

                        // If the width is less than the text width, increase the height to accommodate the text
                        if (params.width < textView.getWidth()) {
                            params.height = Math.max(textHeight, textView.getHeight());
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
                        borderLayout.setLayoutParams(params);

                        // Adjust the text size based on the height and width limits
                        float textSize = textLayout.getTextView().getTextSize();
                        float newSize = textSize;

                        if (params.height > textHeight) {
                            newSize = textSize + dy / 6f;
                        }

                        // Check for maximum text size
                        if (newSize > MAX_TEXT_SIZE) {
                            newSize = MAX_TEXT_SIZE;
                        }
                        if (newSize < 45) {
                            newSize = 45;
                        }

                        // Adjust text size based on the height and width limits
                        float maxWidthBasedSize = Math.min(params.width, params.height);
                        if (newSize > maxWidthBasedSize) {
                            newSize = maxWidthBasedSize;
                        }

                        textLayout.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);

                        lastX = newX;
                        lastY = newY;
                        break;
                }

                return true;
            }
        });


        saveButton = new Button(this);
        textLayout.setSaveButton(saveButton);
        saveButton.setBackgroundResource(R.drawable.checked);
        saveButton.setScaleX(0.324f);
        saveButton.setScaleY(0.324f);
        FrameLayout.LayoutParams saveButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        saveButtonParams.setMargins(-75,0,0,-50);
        saveButtonParams.gravity = Gravity.BOTTOM | Gravity.START;
        saveButton.setLayoutParams(saveButtonParams);
        if(selectedLayer != null && textLayout.getFrameLayout() != null){
            textLayout.getSaveButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textLayout.getResizeButton().setVisibility(View.INVISIBLE);
                    textLayout.getDeleteButton().setVisibility(View.INVISIBLE);
                    textLayout.getRotateButton().setVisibility(View.INVISIBLE);
                    textLayout.getSaveButton().setVisibility(View.INVISIBLE);
                    textLayout.getFrameLayout().setBackground(null);
                }
            });

        }
        if(selectedLayer != null && textLayout.getFrameLayout() != null){

        textLayout.setTextView(textView);



        textLayout.getTextView().setOnTouchListener(new View.OnTouchListener() {
            private float lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        textLayout.getFrameLayout().setBackgroundResource(R.drawable.border_style);
                        textLayout.getSaveButton().setVisibility(View.VISIBLE);
                        textLayout.getResizeButton().setVisibility(View.VISIBLE);
                        textLayout.getDeleteButton().setVisibility(View.VISIBLE);
                        textLayout.getRotateButton().setVisibility(View.VISIBLE);
                        return true;
                    case MotionEvent.ACTION_MOVE:
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
                return true;
            }
        });  };

        // Inside the onTouchListener for the frameLayout
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            private double startAngle = 0;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x = event.getRawX();
                float y = event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        startAngle = Math.toDegrees(Math.atan2(y - view.getPivotY(), x - view.getPivotX()));
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();

                        double angle = Math.toDegrees(Math.atan2(y - view.getPivotY(), x - view.getPivotX())) - startAngle;
                        view.setRotation((float) angle);
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
        // Add the border layout and the resize button to the FrameLayout
        frameLayout.addView(borderLayout);
        frameLayout.addView(textLayout.getResizeButton());
        frameLayout.addView(textLayout.getDeleteButton());
        frameLayout.addView(textLayout.getRotateButton());
//        frameLayout.addView(textView);
        frameLayout.addView(textLayout.getSaveButton());


        // Set the position of the FrameLayout on the screen
        frameLayout.setX(x);
        frameLayout.setY(y);
        // Set an OnTouchListener for the FrameLayout to enable dragging
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            private float dX, dY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x = event.getRawX();
                float y = event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isViewInBounds(view, (int) event.getRawX(), (int) event.getRawY())) {
                            dX = view.getX() - x;
                            dY = view.getY() - y;
                            return true;
                        }
                        return false;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(x + dX)
                                .y(y + dY)
                                .setDuration(0)
                                .start();
                        return true;
                }
                return false;
            }
        });
        return textLayout;
    }
    void addAction(CustomAction action) {
        if (currentActionIndex < actions.size() - 1) {
            actions.subList(currentActionIndex + 1, actions.size()).clear();
        }
        actions.add(action);
        currentActionIndex = actions.size() - 1;
    }

    private void undo() {
        if (currentActionIndex >= 0) {
            CustomAction action = actions.get(currentActionIndex);
            action.undo();
            currentActionIndex--;
        }
    }

    private void redo() {
        if (currentActionIndex < actions.size() - 1) {
            currentActionIndex++;
            CustomAction action = actions.get(currentActionIndex);
            action.redo();
        }
    }

    static class CustomAction {
        private final Runnable undoAction;
        private final Runnable redoAction;
        public CustomAction(Runnable undoAction, Runnable redoAction) {
            this.undoAction = undoAction;
            this.redoAction = redoAction;
        }
        public void undo() {
            undoAction.run();
        }
        public void redo() {
            redoAction.run();
        }
    }
    private double getAngle(double x, double y, float pivotX, float pivotY) {
        double rad = Math.atan2(y - pivotY, x - pivotX) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }
    private boolean isViewInBounds(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        return (x >= viewX && x <= (viewX + viewWidth)) && (y >= viewY && y <= (viewY + viewHeight));
    }






    public FrameLayout getFrameLayout(){
        return frameLayout;
    }
}