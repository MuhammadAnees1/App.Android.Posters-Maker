package com.example.postersmaker;

import static com.example.postersmaker.MainActivity.imageView;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ResizeTouchListener implements View.OnTouchListener {
    private float lastX = 0f, lastY = 0f;
    private boolean isResizingText = false;
    private TextLayout textLayout;
    float newX,newY;
    public ResizeTouchListener(TextLayout textLayout) {
        this.textLayout = textLayout;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectLayer(textLayout);
                if (lastX == 0f && lastY == 0f) {
                    lastX = textLayout.getFrameLayout().getX();
                    lastY = textLayout.getFrameLayout().getY();
                } else {
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                }
                callSetDefaultState();

                // Determine whether resizing text or image based on the content
                isResizingText = textLayout.isTextViewVisible();
                break;

            case MotionEvent.ACTION_UP:
                // Additional logic for handling image resizing, if applicable
                break;

            case MotionEvent.ACTION_MOVE:
                 newX = event.getRawX();
                 newY = event.getRawY();

                float dx = newX - lastX;
                float dy = newY - lastY;

                if (isResizingText) {
                    // Handle resizing logic for text
                    handleTextResize(textLayout, dx, dy);
                } else {
                    // Handle resizing logic for image
                    // handleImageResize(textLayout, dx, dy);
                    // Additional logic for handling image resizing, if applicable
                }

                lastX = newX;
                lastY = newY;
                break;
        }
        return true;
    }

    private void handleTextResize(TextLayout textLayout, float dx, float dy) {
        // Handle text resizing logic

                        // Calculate the direction of resizing based on the current rotation angle
                        float currentRotation = textLayout.getFrameLayout().getRotation();
                        double angleInRadians = Math.toRadians(currentRotation);
                        float cosTheta = (float) Math.cos(angleInRadians);
                        float sinTheta = (float) Math.sin(angleInRadians);

                        // Calculate the relative movement in the rotated coordinates
                         dx = (newX - lastX) * cosTheta + (newY - lastY) * sinTheta;
                         dy = -(newX - lastX) * sinTheta + (newY - lastY) * cosTheta;

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
                                newSize = textSize + dy / 7f;
                            }
                        } else if (dy < 0) {
                            if (textLayout.getFrameLayout().getWidth() < imageView.getWidth() - pxTodp(24)) {
                                newSize = textSize + dy / 7f;
                            }
                        }
                        textLayout.getBorderLayout().setLayoutParams(params);
                        // Check for maximum text size
                        if (newSize > 350) {
                            newSize = 350;
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
 }

    private void handleImageResize(TextLayout textLayout, float dx, float dy) {
        // Handle image resizing logic
        // Implement logic for resizing image in the frame layout
        //  // ...
    }

    private void selectLayer(TextLayout textLayout) {
        // Implement the logic for selecting the layer
        // ...
    }

    private void callSetDefaultState() {
        // Implement the logic for setting the default state
        // ...
    }
    private int pxTodp(int px) {
        // Convert pixels to dp
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(px / density);
    }
}
