package com.example.postersmaker;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomAdapter.OnItemSelected {
    private final CustomAdapter customAdapter = new CustomAdapter(this);
    private final List<FrameLayout> textLayoutList = new ArrayList<>();
    float dX = 0, dY = 0;
    private final List<CustomAction> actions = new ArrayList<>();
    private int currentActionIndex = -1;
    private ImageView imgUndo;
    private ImageView imgRedo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.previewImageView);
        imageView.setImageResource(R.drawable.blank);
        RecyclerView recyclerView = findViewById(R.id.rvConstraintTools);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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

        ViewGroup rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                for (FrameLayout textLayout : textLayoutList) {
                    float x = event.getX();
                    float y = event.getY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            textLayout.setX(x - textLayout.getWidth() / 2);
                            textLayout.setY(y - textLayout.getHeight() / 2);
                            break;
                    }
                }
                return true;
            }
        });
    }
    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case TEXT:
                TextHandlerClass.showTextDialog(this, textLayoutList, (ViewGroup) findViewById(android.R.id.content));
                break;
            case ERASER:
            case FILTER:
            case STICKER:
                // Implement as needed
                break;
        }
    }
    private void showTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Text");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                addTextToImage(text, 100, 100); // Default position
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void addTextToImage(String text, float x, float y) {
        FrameLayout textLayout = createTextLayout(text, x, y);
        textLayoutList.add(textLayout);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.addView(textLayout);


        addAction(new CustomAction(
                // Define the undo logic here
                () -> {
                    // Define how to undo the action
                    viewGroup.removeView(textLayout);
                    textLayoutList.remove(textLayout);
                },
                // Define the redo logic here
                () -> {
                    // Define how to redo the action
                    viewGroup.addView(textLayout);
                    textLayoutList.add(textLayout);
                }
        ));
    }
    @SuppressLint("ClickableViewAccessibility")
    FrameLayout createTextLayout(String text, float x, float y) {
        FrameLayout frameLayout = new FrameLayout(this);

        // Create a FrameLayout to hold the TextView and the button
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        FrameLayout.LayoutParams deleteButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteButtonParams.gravity = Gravity.TOP | Gravity.START;
        deleteButton.setLayoutParams(deleteButtonParams);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                viewGroup.removeView(frameLayout);
                textLayoutList.remove(frameLayout);
            }
        });
        Button rotateButton = new Button(this);
        rotateButton.setText("Rotate");
        FrameLayout.LayoutParams rotateButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rotateButtonParams.gravity = Gravity.TOP | Gravity.END;
        rotateButton.setLayoutParams(rotateButtonParams);

        rotateButton.setOnTouchListener(new View.OnTouchListener() {
            private double startAngle;
            final float rotationSpeed = 0.0285f;
            private boolean isRotating = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startAngle = getAngle(event.getX(), event.getY());
                            isRotating = true;
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            if (isRotating) {
                                double currentAngle = getAngle(event.getX(), event.getY());
                                float rotation = (float) Math.toDegrees(currentAngle - startAngle) * rotationSpeed;
                                frameLayout.setRotation(rotation);
                            }
                            return true;

                        case MotionEvent.ACTION_UP:
                            isRotating = false;
                            return true;
                    }
                    return false;
                }
            });
        // Create the TextView
        EditText textView = new EditText(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.NORMAL);

        // Set the position of the TextView within the FrameLayout
        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(textLayoutParams);
        textLayoutParams.gravity = Gravity.CENTER;

        // Add a visible border around the text layer
        LinearLayout borderLayout = new LinearLayout(this);
        borderLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        borderLayout.setBackgroundResource(R.drawable.border_style);
        borderLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        // Add the TextView to the border layout
        borderLayout.addView(textView);

        // Create a button to resize the text
        Button resizeButton = new Button(this);
        resizeButton.setText("resize");
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.BOTTOM | Gravity.END;
        buttonParams.setMargins(0, 0, -20, -20);
        resizeButton.setLayoutParams(buttonParams);
        // Set the OnTouchListener for the resize button
        resizeButton.setOnTouchListener(new View.OnTouchListener() {
            private float lastX, lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
                        float newX = event.getRawX();
                        float newY = event.getRawY();
                        float dx = newX - lastX;
                        float dy = newY - lastY;

                        float ratio = textView.getWidth()* 1.0f / textView.getHeight();
                        float change;
                        if (Math.abs(dx) > Math.abs(dy)) {
                            change = dx;
                        } else {
                            change = dy;
                        }

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) borderLayout.getLayoutParams();
                        params.width += change;
                        params.height = (int) (params.width / ratio);

                        if (params.height < 10) {
                            params.height = 10;
                            params.width = (int) (params.height * ratio);
                        }

                        borderLayout.setLayoutParams(params);

                        float textSize = textView.getTextSize();
                        if (change > 0) {
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.max(textSize + 10, 10 * getResources().getDisplayMetrics().scaledDensity));
                        } else {
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.max(textSize - 10, 10 * getResources().getDisplayMetrics().scaledDensity));
                        }
                        lastX = newX;
                        lastY = newY;
                        break;
                }
                return true;
            }
        });

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
        frameLayout.addView(resizeButton);
        frameLayout.addView(deleteButton);
        frameLayout.addView(rotateButton);
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
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        return frameLayout;
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
    private double getAngle(double x, double y) {
        FrameLayout frameLayout = new FrameLayout(this);
        double rad = Math.atan2(y - frameLayout.getHeight() / 2, x - frameLayout.getWidth() / 2) + Math.PI;
        frameLayout.setRotation(0.02f);
        return (rad * 180 / Math.PI + 180) % 360;
    }

}
