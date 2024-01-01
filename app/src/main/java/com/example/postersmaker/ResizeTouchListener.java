package com.example.postersmaker;

import static com.example.postersmaker.MainActivity.callSetDefaultState;
import static com.example.postersmaker.MainActivity.getAngle;
import static com.example.postersmaker.MainActivity.imageView;
import static com.example.postersmaker.MainActivity.selectLayer;
import static com.example.postersmaker.MainActivity.selectLayers;
import static com.example.postersmaker.MainActivity.selectedLayer;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ResizeTouchListener implements View.OnTouchListener {
    private float lastX = 0f, lastY = 0f;
    private TextLayout textLayout;
    private ImageLayout imageLayout;
    float newX, newY;
    // Constructor for TextLayout
    public ResizeTouchListener(TextLayout textLayout) {
        this.textLayout = textLayout;
        // Initialize lastX and lastY here
        lastX = textLayout.getFrameLayout().getX();
        lastY = textLayout.getFrameLayout().getY();
    }

    // Constructor for ImageLayout
    public ResizeTouchListener(ImageLayout imageLayout) {
        this.imageLayout = imageLayout;
        // Initialize lastX and lastY here
        lastX = imageLayout.getFrameLayout().getX();
        lastY = imageLayout.getFrameLayout().getY();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (textLayout != null) {
                    MainActivity.selectLayer(textLayout);
                    if (lastX == 0f && lastY == 0f) {
                        lastX = textLayout.getFrameLayout().getX();
                        lastY = textLayout.getFrameLayout().getY();
                    } else {
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                    }
                } else if (imageLayout != null) {
                    selectLayers(imageLayout);
                    callSetDefaultState();
                    if (lastX == 0f && lastY == 0f) {
                        lastX = imageLayout.getFrameLayout().getX();
                        lastY = imageLayout.getFrameLayout().getY();
                    }
                    else {
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                // Additional logic for handling image resizing, if applicable
                break;

            case MotionEvent.ACTION_MOVE:
                newX = event.getRawX();
                newY = event.getRawY();

                if (textLayout != null) {
                    handleTextResize(textLayout);
                } else if (imageLayout != null) {
                    handleImageResize(imageLayout, true, newX, newY, lastX, lastY);
                }
                lastX = newX;
                lastY = newY;
                break;
        }
        return true;
    }
    private void handleTextResize(TextLayout textLayout   ) {

        // Calculate the direction of resizing based on the current rotation angle
        float currentRotation = textLayout.getFrameLayout().getRotation();
        double angleInRadians = Math.toRadians(currentRotation);
        float cosTheta = (float) Math.cos(angleInRadians);
        float sinTheta = (float) Math.sin(angleInRadians);

        // Calculate the relative movement in the rotated coordinates
        float dx = (newX - lastX) * cosTheta + (newY - lastY) * sinTheta;
        float  dy = -(newX - lastX) * sinTheta + (newY - lastY) * cosTheta;

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
                newSize = textSize + dy / 5f;
            }
        } else if (dy < 0) {
            if (textLayout.getFrameLayout().getWidth() < imageView.getWidth() - pxTodp(24)) {
                newSize = textSize + dy / 5f;
            }
        }
        textLayout.getBorderLayout().setLayoutParams(params);

        // Check for maximum text size
        if (newSize > 250) {
            newSize = 250;
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

//           handleResize(textLayout.getFrameLayout(), true);
    }
    private void handleImageResize(ImageLayout imageLayout, boolean isResizable, float newX, float newY, float lastX, float lastY) {
        // Get the FrameLayout containing the image
        FrameLayout frameLayout = imageLayout.getFrameLayout();
        ImageView imageView1 = imageLayout.getImageView();
        float currentRotation = imageLayout.getFrameLayout().getRotation();
        double angleInRadians = Math.toRadians(currentRotation);
        float cosTheta = (float) Math.cos(angleInRadians);
        float sinTheta = (float) Math.sin(angleInRadians);

        // Calculate the relative movement in the rotated coordinates
        float dx = (newX - lastX) * cosTheta + (newY - lastY) * sinTheta;
        float  dy = -(newX - lastX) * sinTheta + (newY - lastY) * cosTheta;

        // Get the current width and height of the FrameLayout
        int currentWidth = frameLayout.getWidth();
        int currentHeight = frameLayout.getHeight();

        int currentWidth1 = imageView1.getWidth();
        int currentHeight1 = imageView1.getHeight();

        int minWidth = pxTodp(300);
        int minHeight = pxTodp(300);
        // Calculate the new dimensions based on the movement
        int newWidth = Math.max((int) (currentWidth + dx), pxTodp(20));
        int newHeight = Math.max((int) (currentHeight + dy), pxTodp(20));
        int newWidth1 = Math.max((int) (currentWidth1 + dx), pxTodp(20));
        int newHeight1 = Math.max((int) (currentHeight1 + dy), pxTodp(20));

// Set the new dimensions within the limits
        RelativeLayout.LayoutParams layoutParamss = (RelativeLayout.LayoutParams) imageView1.getLayoutParams();
        layoutParamss.width = Math.max(minWidth, Math.min(newWidth1, newHeight1));
        layoutParamss.height = Math.max(minHeight, Math.min(newWidth1, newHeight1));

// Set the new dimensions within the limits
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.width = Math.max(minWidth, Math.min(newWidth, newHeight));
        layoutParams.height = Math.max(minHeight, Math.min(newWidth, newHeight));

        if (isResizable) {
            // Additional logic for resizable layouts
            // Adjust width and height separately (you can customize this logic)
            layoutParams.width = Math.max(minWidth, Math.min(layoutParams.width, layoutParams.height));
            layoutParams.height = Math.max(minHeight, Math.min(layoutParams.width, layoutParams.height));

            layoutParamss.width = Math.max(minWidth, Math.min(layoutParamss.width, layoutParamss.height));
            layoutParamss.height = Math.max(minHeight, Math.min(layoutParamss.width, layoutParamss.height));
        }

// Update the FrameLayout with the new dimensions
        imageView1.setLayoutParams(layoutParamss);
        frameLayout.setLayoutParams(layoutParams);

    }

    // Helper function to convert pixels to dp
    private int pxTodp(int px) {
        // Convert pixels to dp
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(px / density);
    }
}