package com.example.postersmaker;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Track {

   String text ;


    boolean lock,fontR =false,rotate = false,istextcolor = false, shadowOn = false, resize = false, position = false ;

    float positionX, shadowDx, shadowDy, spacing, rotation, shadow, textSize, alpha, positionY;

    int Tid, textColor, textAlignment, height, imgwidth, imgheight, width ;
    Typeface typeface;
    static List<Track> list = new ArrayList<>();



    public boolean isFontR() {return fontR;}
    public boolean isShadowOn() {return shadowOn;}
    public int getImgwidth() {
        return imgwidth;
    }
    public int getImgheight() {
        return imgheight;
    }
    public Typeface getTypeface() {return typeface;}
    public String getText() {
        return text;
    }
    public boolean isIstextcolor() {
        return istextcolor;
    }
    public float getPositionX() {
        return positionX;
    }
    public float getPositionY() {
        return positionY;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getTextAlignment() {
        return textAlignment;
    }
    public float getTextSize() {
        return textSize;
    }
    public float getAlpha() {return alpha;}
    public float getShadow() {
        return shadow;
    }
    public float getRotation() {
        return rotation;
    }
    public float getSpacing() {
        return spacing;
    }
    public boolean isLock() {
        return lock;
    }
    public float getShadowDx() {
        return shadowDx;
    }
    public float getShadowDy() {
        return shadowDy;
    }
    public int getTextColor() {
        return textColor;
    }
    public int getTid() {
        return Tid;
    }
    public static List<Track> getList() {
        return list;
    }
    public boolean getPosition() {return position;}
    public boolean isRotate() {
        return rotate;
    }
    public boolean isResize() {
        return resize;
    }




     public Track(int tid, float PositionX, float PositionY, boolean position )
     {
         this.Tid = tid;
         this.positionX = PositionX;
         this.positionY = PositionY;
         this.position = position;

     }
     public Track(int tid, float rotation, boolean rotate )
     {
         this.Tid = tid;
         this.rotation = rotation;
         this.rotate = rotate;
     }
     public Track(int tid, int width, int height,int imgwidth,int imgheight, float textSize,boolean resize ){
         this.Tid = tid;
         this.width = width;
         this.height = height;
         this.textSize = textSize;
         this.resize = resize;
         this.imgwidth = imgwidth;
         this.imgheight = imgheight;
     }
     public Track(int tid, boolean istextcolor, int textColor){
         this.Tid = tid;
         this.istextcolor = istextcolor;
         this.textColor = textColor;
     }
     public Track(int tid, float shadow, float shadowDx, float shadowDy,boolean isShadowon ){
         this.Tid = tid;
         this.shadow = shadow;
         this.shadowDx = shadowDx;
         this.shadowDy = shadowDy;
         this.shadowOn = isShadowon;
     }
     public Track( int tid, boolean fontR, Typeface typeface ){
         this.Tid = tid;
         this.fontR = fontR;
         this.typeface = typeface;

     }


}
