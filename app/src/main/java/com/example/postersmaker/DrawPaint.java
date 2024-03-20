package com.example.postersmaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DrawPaint extends View {

    private List<Pair<Path, Paint>> paths = new ArrayList<>();
    private Paint paint = new Paint();
    static Bitmap bitmap1 = null;

    static boolean brushEnable = false;
    static  int size = 10;
    static int color = Color.BLACK;

    private float eventX, eventY, lastTouchX, lastTouchY;
    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private final RectF dirtyRect = new RectF();

    public DrawPaint(Context context) {
        super(context);
        init(context, null);
    }

    public DrawPaint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DrawPaint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void eraseDrawing() {
        if(paths.size() > 0){
        paths.remove(paths.size() - 1);}
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        for (Pair<Path, Paint> pathPaintPair : paths) {
            canvas.drawPath(pathPaintPair.first, pathPaintPair.second);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!brushEnable) {
            return false;
        }

        eventX = event.getX();
        eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Create a new Paint object for the current color
                Paint newPathPaint = new Paint();
                newPathPaint.set(paint);
                newPathPaint.setColor(color);
                newPathPaint.setStrokeWidth(size);

                // Start a new path with the new Paint object
                Path newPath = new Path();
                newPath.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;

                // Add the new path and paint to the list
                paths.add(new Pair<>(newPath, newPathPaint));
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    paths.get(paths.size() - 1).first.lineTo(historicalX, historicalY);
                }
                paths.get(paths.size() - 1).first.lineTo(eventX, eventY);
                break;

            default:
                return false;
        }

        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;
        getbitmap();

        return true;
    }

    private void init(Context context, AttributeSet attrs) {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(color);
    }

    private void resetDirtyRect(float eventX, float eventY) {
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }

        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    private Bitmap getDrawnBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }

    public void getbitmap() {
        bitmap1 = getDrawnBitmap();
    }
}
