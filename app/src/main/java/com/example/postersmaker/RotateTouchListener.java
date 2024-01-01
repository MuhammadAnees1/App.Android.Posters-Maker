package com.example.postersmaker;

import static com.example.postersmaker.MainActivity.getAngle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class RotateTouchListener implements View.OnTouchListener {
    private double startAngle;
    final float rotationSpeed = 0.0238f;
    private TextLayout textLayout;
    private ImageLayout imageLayout;

    // Constructor for text layout
    public RotateTouchListener(TextLayout textLayout) {
        this.textLayout = textLayout;
    }

    // Constructor for image layout
    public RotateTouchListener(ImageLayout imageLayout) {
        this.imageLayout = imageLayout;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (textLayout != null) {
                    MainActivity.selectLayer(textLayout);
                    MainActivity.callSetDefaultState();
                    // Store the initial rotation angle
                    startAngle = getAngle((event.getX() / 10), (event.getY() / 10), textLayout.getFrameLayout().getPivotX(), textLayout.getFrameLayout().getPivotY());
                } else if (imageLayout != null) {
                    // Handle image layout touch
                    MainActivity.selectLayers(imageLayout);
                    MainActivity.callSetDefaultState();
                    startAngle = getAngle((event.getX() / 10), (event.getY() / 10), imageLayout.getFrameLayout().getPivotX(), imageLayout.getFrameLayout().getPivotY());
                }
                return true;

            case MotionEvent.ACTION_UP:
                // Additional logic for handling image resizing, if applicable
                break;

            case MotionEvent.ACTION_MOVE:
                if (textLayout != null && textLayout.isTextViewVisible()) {
                    // Handle rotating logic for text
                    handleRotate(event, textLayout.getFrameLayout());
                } else if (imageLayout != null) {
                    // Handle rotating logic for image
                    handleRotate(event, imageLayout.getFrameLayout());
                    // Additional logic for handling image resizing, if applicable
                }
                break;
        }
        return true;
    }

    private void handleRotate(MotionEvent event, FrameLayout frameLayout) {
        double currentAngle = getAngle((event.getX() / 10), (event.getY() / 10), frameLayout.getPivotX(), frameLayout.getPivotY());

        // Calculate the angle difference and apply the rotation speed factor
        float newRotation = (float) (Math.toDegrees(currentAngle - startAngle) * rotationSpeed);
        float currentRotation = frameLayout.getRotation();
        currentRotation += newRotation;

        // Apply the new rotation to the FrameLayout
        frameLayout.setRotation(currentRotation);
    }
}

