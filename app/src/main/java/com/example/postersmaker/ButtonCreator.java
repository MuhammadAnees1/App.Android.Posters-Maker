package com.example.postersmaker;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class ButtonCreator {
    public static Button createRotateButton(Context context, float scaleX, float scaleY, int marginStart, int marginTop) {
        Button rotateButton = createButton(context, R.drawable.rotate, scaleX, scaleY);
        setButtonLayoutParams(rotateButton, marginStart, marginTop,0,0,Gravity.TOP | Gravity.START);
        return rotateButton;
    }

    public static Button createDeleteButton(Context context, float scaleX, float scaleY, int marginEnd, int marginTop) {
        Button deleteButton = createButton(context, R.drawable.close, scaleX, scaleY);
        setButtonLayoutParams(deleteButton, 0, marginTop,marginEnd,0, Gravity.TOP | Gravity.END);
        return deleteButton;
    }

    public static Button createResizeButton(Context context, float scaleX, float scaleY, int marginEnd, int marginBottom) {
        Button resizeButton = createButton(context, R.drawable.resize, scaleX, scaleY);
        setButtonLayoutParams(resizeButton, 0, 0,marginEnd,marginBottom, Gravity.BOTTOM | Gravity.END);
        return resizeButton;
    }

    public static Button createSaveButton(Context context, float scaleX, float scaleY, int marginStart, int marginBottom) {
        Button saveButton = createButton(context, R.drawable.checked, scaleX, scaleY);
        setButtonLayoutParams(saveButton, marginStart, 0,0,marginBottom, Gravity.BOTTOM | Gravity.START);
        return saveButton;
    }

    private static Button createButton(Context context, int backgroundResource, float scaleX, float scaleY) {
        Button button = new Button(context);
        button.setBackgroundResource(backgroundResource);
        button.setScaleX(scaleX);
        button.setScaleY(scaleY);
        return button;
    }
    private static void setButtonLayoutParams(Button button, int marginStart, int marginTop,int marginEnd,int marginBottom, int gravity) {
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonParams.gravity = gravity;
        buttonParams.setMargins(pxTodp(button.getContext(), marginStart), pxTodp(button.getContext(), marginTop), pxTodp(button.getContext(), marginEnd), pxTodp(button.getContext(), marginBottom));
        button.setLayoutParams(buttonParams);
    }
    private static int pxTodp(Context context, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }
}
