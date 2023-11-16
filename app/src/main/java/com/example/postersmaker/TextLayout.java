package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TextLayout {


    private FrameLayout frameLayout;
    private RelativeLayout borderLayout;
    private Button deleteButton;
    private Button rotateButton;
    private Button resizeButton;
    private Button saveButton;
    private TextView textView;
    private int shadowWidth = 20;
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

        textPaint.setColor(Color.BLACK);
    }

    public int getShadowWidth() {
        return shadowWidth;
    }

    public void setShadowWidth(int shadowWidth) {
        this.shadowWidth = shadowWidth;
        applyShadowEffect(textView, shadowWidth, Color.RED);
        invalidate();
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
    public  float getStrokeWidth() {
        return textPaint.getStrokeWidth();
    }
    public  void setStrokeColor(int strokeColor) {
        textPaint.setColor(strokeColor);
    }

    public void setStrokeType(StrokeType strokeType) {
        // Set the stroke type (line, dash, dot)
        switch (strokeType) {
            case LINE:
                Toast.makeText(frameLayout.getContext(), "lines", Toast.LENGTH_SHORT).show();
                applyShadowEffect(textView,getShadowWidth() , Color.RED); // Replace 5 with your desired shadow width

                Log.d(TAG, "setStrokeType: " + textPaint);
                break;

            case DASH:
                applyDottedStroke(textView, 20, Color.RED);

                break;
            case DOT:
                // Apply dot stroke to the round of the text
                textPaint.setStyle(Paint.Style.STROKE);
                // You can set custom dot effects if needed
                textPaint.setPathEffect(new DashPathEffect(new float[]{1, 5}, 0));
                textPaint.setColor(Color.BLACK);
                break;
            default:
                // Default case, reset to normal
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setPathEffect(null);
                textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }
        invalidate();
    }
    private void applyDottedStroke(TextView textView, float strokeWidth, int strokeColor) {
        Paint paint = textView.getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(strokeColor);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        textView.setTextColor(strokeColor); // Set text color to the stroke color

        // You can adjust the stroke width and color as needed
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
    private void applyShadowEffect(TextView textView, float shadowWidth, int shadowColor) {
        textView.setShadowLayer(shadowWidth, 0, 0, shadowColor);
    }

    private void invalidate() {
        // Implement your custom invalidation logic here
        frameLayout.invalidate();
    }
}