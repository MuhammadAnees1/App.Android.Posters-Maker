package com.example.postersmaker;


import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ResizeButtonHandler {
    private EditText editText;
    private LinearLayout borderLayout;

    public ResizeButtonHandler(EditText editText, LinearLayout borderLayout) {
        this.editText = editText;
        this.borderLayout = borderLayout;
    }

    public Button createResizeButton(Context context) {
        Button resizeButton = new Button(context);
        resizeButton.setText("resize");
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.BOTTOM | Gravity.END;
        buttonParams.setMargins(0, 0, -20, -20);
        resizeButton.setLayoutParams(buttonParams);
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

                        float ratio = editText.getWidth() * 1.0f / editText.getHeight();
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

                        float textSize = editText.getTextSize();
                        if (change > 0) {
                            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.max(textSize + 10, 10 * context.getResources().getDisplayMetrics().scaledDensity));
                        } else {
                            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.max(textSize - 10, 10 * context.getResources().getDisplayMetrics().scaledDensity));
                        }
                        lastX = newX;
                        lastY = newY;
                        break;
                }
                return true;
            }
        });
        return resizeButton;
    }
}
