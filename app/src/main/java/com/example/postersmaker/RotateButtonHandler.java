package com.example.postersmaker;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class RotateButtonHandler {
    private FrameLayout frameLayout;
    private double startAngle;
    private static final float ROTATION_SPEED = 0.008f; // Adjust the speed as needed

    public RotateButtonHandler(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public Button createRotateButton(Context context) {
        Button rotateButton = new Button(context);
        rotateButton.setText("Rotate");
        FrameLayout.LayoutParams rotateButtonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rotateButtonParams.gravity = Gravity.TOP | Gravity.END;
        rotateButton.setLayoutParams(rotateButtonParams);
        rotateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startAngle = getAngle(event.getX(), event.getY());
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        double currentAngle = getAngle(event.getX(), event.getY());
                        float rotation = (float) Math.toDegrees(currentAngle - startAngle) * ROTATION_SPEED;
                        frameLayout.setRotation(rotation);
                        return true;

                    case MotionEvent.ACTION_CANCEL:
                        // Handle the action when the touch is released
                        return true;
                }
                return false;
            }
        });
        return rotateButton;
    }

    private double getAngle(double x, double y) {
        double rad = Math.atan2(y - frameLayout.getHeight() / 2, x - frameLayout.getWidth() / 2) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }
}
