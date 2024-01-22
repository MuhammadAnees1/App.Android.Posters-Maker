package com.example.postersmaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {
    private static final float SHARP_POINT_LENGTH = 20;
    private Paint paint;
    private Path path;

    private float previousX, previousY;
    private long previousTimestamp;
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        // Initialize Paint object
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND); // Use ROUND for tapered effect
        paint.setStrokeWidth(10f);
        // Initialize Path object
        path = new Path();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the path
        canvas.drawPath(path, paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }

        return true;
    }

    private void touchStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        previousX = x;
        previousY = y;
        previousTimestamp = System.currentTimeMillis();
    }

    private void touchMove(float x, float y) {
        long currentTime = System.currentTimeMillis();
        float velocity = calculateVelocity(x, y, currentTime);

        // Update path
        path.quadTo(previousX, previousY, (x + previousX) / 2, (y + previousY) / 2);

        // Draw a sharp point if velocity is high
        if (velocity > 1000) {
            drawSharpPoint(x, y);
        }

        // Invalidate the view to trigger onDraw
        invalidate();

        // Update previous values
        previousX = x;
        previousY = y;
        previousTimestamp = currentTime;
    }

    private void touchUp() {
        // Reset paint to use ROUND cap when not drawing
        paint.setStrokeCap(Paint.Cap.ROUND);
    }
    private void drawSharpPoint(float x, float y) {
        // Calculate the angle of the line
        float angle = (float) Math.atan2(y - previousY, x - previousX);

        // Calculate the sharp point coordinates
        float sharpPointX = x - SHARP_POINT_LENGTH * (float) Math.cos(angle);
        float sharpPointY = y - SHARP_POINT_LENGTH * (float) Math.sin(angle);

        // Move to the sharp point and draw a line to it
        path.lineTo(sharpPointX, sharpPointY);
    }
    private float calculateVelocity(float x, float y, long currentTime) {
        float distance = (float) Math.hypot(x - previousX, y - previousY);
        long timeDelta = currentTime - previousTimestamp;

        // Calculate velocity in pixels per second
        return (distance / timeDelta) * 1000;
    }
}
