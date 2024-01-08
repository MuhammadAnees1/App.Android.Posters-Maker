package com.example.postersmaker;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurProcessor {
    private RenderScript rs;
    private ScriptIntrinsicBlur script;

    public BlurProcessor(Context context) {
        rs = RenderScript.create(context);
        script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
    }

    public Bitmap blur(Bitmap inputBitmap, float blurRadius) {
        Allocation inputAllocation = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation outputAllocation = Allocation.createTyped(rs, inputAllocation.getType());

        script.setRadius(blurRadius);
        script.setInput(inputAllocation);
        script.forEach(outputAllocation);

        outputAllocation.copyTo(inputBitmap);

        return inputBitmap;
    }
}

