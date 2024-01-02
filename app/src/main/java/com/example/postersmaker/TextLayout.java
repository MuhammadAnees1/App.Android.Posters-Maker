package com.example.postersmaker;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class TextLayout {
    private FrameLayout frameLayout;
    private RelativeLayout borderLayout;
    private Button deleteButton;
    private Button rotateButton;
    private Button resizeButton;
    DottedStrokeTextView textView2;
    private Button saveButton;
    private TextView textView;
    private Boolean isLocked ;
    Paint textPaint;

    public TextLayout(FrameLayout frameLayout, RelativeLayout borderLayout, Button deleteButton, Button rotateButton, Button resizeButton, Button saveButton, TextView textView , Boolean isLocked) {
        this.frameLayout = frameLayout;
        this.borderLayout = borderLayout;
        this.deleteButton = deleteButton;
        this.rotateButton = rotateButton;
        this.resizeButton = resizeButton;
        this.saveButton = saveButton;
        this.textView = textView;
        this.textPaint = new Paint();
        this.textView2 = new DottedStrokeTextView(frameLayout.getContext());
        this.isLocked = isLocked;
        textPaint.setColor(Color.BLACK);
    }
    public Boolean getLocked() {
        return isLocked;
    }
    public void setLocked(Boolean locked) {
        isLocked = locked;
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
                Paint paint = textView2.getPaint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(getStrokeWidth());
                paint.setColor(Color.BLACK);
                textView2.invalidate(); // Invalidate to trigger a redraw with the new stroke effect
                Log.d(TAG, "setStrokeType: " + textPaint);
                break;

            case DASH:
                // Apply dot stroke to the round of the text
                textPaint.setStyle(Paint.Style.STROKE);
                // You can set custom dot effects if needed
                textPaint.setPathEffect(new DashPathEffect(new float[]{1, 5}, 0));
                textPaint.setColor(Color.BLACK);
                break;
            case DOT:
                applyDottedStroke(textView2, 20, Color.RED);
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
    private void applyDottedStroke(DottedStrokeTextView textView2, float strokeWidth, int strokeColor) {
        Paint paint = textView2.getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(strokeColor);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        textView2.setTextColor(strokeColor); // Set text color to the stroke color
        textView2.invalidate(); // Invalidate to trigger a redraw with the new stroke effect
    }
    private void applyShadowEffect(TextView textView, float shadowWidth, int shadowColor) {
        textView.setShadowLayer(shadowWidth, 0, 0, shadowColor);
    }
    private void invalidate() {
        // Implement your custom invalidation logic here
        frameLayout.invalidate();
    }
    public boolean isTextViewVisible() {
        // Check if the TextView is visible or not
        return textView.getVisibility() == View.VISIBLE;
    }
    public class DottedStrokeTextView extends androidx.appcompat.widget.AppCompatTextView {

        private Paint dottedStrokePaint;

        public DottedStrokeTextView(Context context) {
            super(context);
            init();
        }

        public DottedStrokeTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DottedStrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }
        private void init() {
            dottedStrokePaint = new Paint();
            dottedStrokePaint.setStyle(Paint.Style.STROKE);
            dottedStrokePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
            dottedStrokePaint.setColor(getCurrentTextColor());
            dottedStrokePaint.setStrokeWidth(5); // Set your desired stroke width
        }
        @Override
        protected void onDraw(Canvas canvas) {
            // Draw the original text
            super.onDraw(canvas);

            // Draw the dotted stroke around the text
            canvas.drawPath(getPath(), dottedStrokePaint);
        }
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            // Adjust the path whenever the size of the view changes
            super.onSizeChanged(w, h, oldw, oldh);
            invalidate();
        }
        private Path getPath() {
            Path path = new Path();
            path.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
            return path;
        }
    }
}