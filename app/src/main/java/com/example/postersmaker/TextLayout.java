package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class  TextLayout {

    private FrameLayout frameLayout;
    private RelativeLayout borderLayout;
    private Button deleteButton;
    private Button rotateButton;
    private Button resizeButton;
    private Button saveButton;
    private TextView textView;
    Paint textPaint;

    public TextLayout(FrameLayout frameLayout, RelativeLayout borderLayout, Button deleteButton, Button rotateButton, Button resizeButton, Button saveButton, TextView textView) {
        this.frameLayout = frameLayout;
        this.borderLayout = borderLayout;
        this.deleteButton = deleteButton;
        this.rotateButton = rotateButton;
        this.resizeButton = resizeButton;
        this.saveButton = saveButton;
        this.textView = textView;
        this.textPaint = new Paint();


    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public RelativeLayout getBorderLayout() {
        return borderLayout;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Button getRotateButton() {
        return rotateButton;
    }

    public void setRotateButton(Button rotateButton) {
        this.rotateButton = rotateButton;
    }

    public Button getResizeButton() {
        return resizeButton;
    }

    public void setResizeButton(Button resizeButton) {
        this.resizeButton = resizeButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public void setBorderLayout(RelativeLayout borderLayout) {
        this.borderLayout = borderLayout;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    // Methods to set X and Y positions
    public float getX() {
        return frameLayout.getX();
    }

    public void setX(float x) {
        frameLayout.setX(x);
    }

    public float getY() {
        return frameLayout.getY();
    }

    public void setY(float y) {
        frameLayout.setY(y);
    }
    public void setStrokeWidth(float strokeWidth) {
        textPaint.setStrokeWidth(strokeWidth);
        // You might perform additional actions here if needed
    }

    public void setStrokeType(StrokeType strokeType) {
        // Set the stroke type (line, dash, dot)
        switch (strokeType) {
            case LINE:
                Toast.makeText(frameLayout.getContext(), "lines", Toast.LENGTH_SHORT).show();

                // Assuming textPaint is defined somewhere in your code
                Paint textPaints = getTextView().getPaint();

                // Increase the text shadow for LINE stroke type
                float shadowValue = 10.0f; // You can adjust this value based on your preference
                textPaints.setShadowLayer(shadowValue, 0, 0, Color.BLACK);

                Log.d(TAG, "onStrokeTypeSelected: " + textPaints);

                // Notify the parent view to request a layout pass
                getFrameLayout().requestLayout();
                break;


            case DASH:
                Toast.makeText(frameLayout.getContext(), "dash", Toast.LENGTH_SHORT).show();

                // Assuming textPaint is defined somewhere in your code
                Paint textsPaint = textView.getPaint();

                // Apply dash stroke to the round of the text
                textsPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                // You can set custom dash effects if needed
                textsPaint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
                textsPaint.setColor(Color.BLACK);

                // Clear the shadow layer for other stroke types
                textsPaint.clearShadowLayer();

                Log.d(TAG, "setStrokeType: " + textsPaint);

                // Notify the parent view to request a layout pass
                frameLayout.requestLayout();
                break;

            case DOT:
                // Apply dot stroke to the round of the text
                textPaint.setStyle(Paint.Style.FILL);
                // You can set custom dot effects if needed
                textPaint.setPathEffect(new DashPathEffect(new float[]{1, 5}, 0));
                textPaint.setColor(Color.BLACK);

                // Clear the shadow layer for other stroke types
                textPaint.clearShadowLayer();
                break;

            default:
                // Default case, reset to normal
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setPathEffect(null);
                textPaint.clearShadowLayer();
                break;
        }
        invalidate();
    }

    private float dpToPx(float dp) {
        float density = frameLayout.getContext().getResources().getDisplayMetrics().density;
        return dp * density;
    }
    private void invalidate() {
        // Implement your custom invalidation logic here
        frameLayout.invalidate();
    }
}