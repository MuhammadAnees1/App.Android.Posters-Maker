//package com.example.postersmaker;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.renderscript.Allocation;
//import android.renderscript.Element;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicBlur;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//
//public class BlurBuilder extends LinearLayout {
//
//    private ImageView imageView;
//    private SeekBar blurSeekBar;
//
//    private float defaultBitmapScale = 0.1f;
//    private static final int MAX_RADIUS = 25;
//    private static final int MIN_RADIUS = 1;
//
//    public BlurBuilder(Context context) {
//        super(context);
//        init(null);
//    }
//
//    public BlurBuilder(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs);
//    }
//
//    public BlurBuilder(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(attrs);
//    }
//
//    private void init(AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
//                    attrs,
//                    R.styleable.BlurBuilder,
//                    0, 0);
//
//            try {
//                int radius = typedArray.getInteger(R.styleable.BlurBuilder_radius, 0);
//                // Use 'radius' as needed
//            } finally {
//                typedArray.recycle();
//            }
//        }
//
//    }
//
//    public void setBlur(int radius) {
//        Drawable imageDrawable = imageView.getDrawable();
//
//        // max radius = 25
//        if (radius > MIN_RADIUS && radius <= MAX_RADIUS && imageDrawable instanceof BitmapDrawable) {
//            Bitmap blurred = blurRenderScript(((BitmapDrawable) imageDrawable).getBitmap(), radius);
//            imageView.setImageBitmap(blurred);
//        } else if (radius == 0) {
//            imageView.setImageDrawable(imageDrawable);
//        } else {
//            Log.e("BLUR", "Invalid radius: " + radius);
//        }
//    }
//
//    public Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {
//        int width = Math.round(smallBitmap.getWidth() * defaultBitmapScale);
//        int height = Math.round(smallBitmap.getHeight() * defaultBitmapScale);
//
//        Bitmap inputBitmap = Bitmap.createScaledBitmap(smallBitmap, width, height, false);
//        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
//
//        RenderScript rs = RenderScript.create(getContext());
//        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//        theIntrinsic.setRadius(radius);
//        theIntrinsic.setInput(tmpIn);
//        theIntrinsic.forEach(tmpOut);
//        tmpOut.copyTo(outputBitmap);
//
//        return outputBitmap;
//    }
//}
