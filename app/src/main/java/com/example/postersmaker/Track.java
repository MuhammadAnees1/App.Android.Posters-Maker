package com.example.postersmaker;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Track {
   String text ;

    TextView textView;
    ImageView imageView;



    boolean lock,fontR =false,rotate = false,space = false, edittext = false,imgOpacity = false, flip = false,isunderline = false,underline = false,
            istextcolor = false, shadowOn = false, resize = false, position = false, issize = false, allign = false;

    float hueValue, positionX, shadowDx, shadowDy, spacing, rotation, shadow,imgopacity1, textSize, alpha, positionY, flipx;

    int Tid, textColor, textAlignment, height, imgwidth, imgheight, width, index;
    String item = null;

    public int getIndex() {
        return index;
    }

    Typeface typeface;
    static List<Track> list = new ArrayList<>();
    static List<Track> list2 = new ArrayList<>();



    public boolean isFontR() {return fontR;}
    public boolean isSpace() {return space;}
    public boolean isEdittext() {
        return edittext;
    }
    public float getImgopacity1() {
        return imgopacity1;
    }
    public boolean isUnderline() {
        return underline;
    }

    public boolean isAllign() {
        return allign;
    }

    public boolean isImgOpacity() {
        return imgOpacity;
    }

    public boolean isIssize() {return issize;}

    public TextView getTextView() {
        return textView;
    }

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

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isFlip() {
        return flip;
    }

    public float getFlipx() {
        return flipx;
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

    public float getHueValue() {
        return hueValue;
    }

    public String getItem() {
        return item;
    }

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

    public boolean isIsunderline() {
        return isunderline;
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


    public Track(boolean issize, int tid, float textSize){
         this.Tid = tid;
         this.textSize = textSize;
         this.issize = issize;

     }
     public Track( float spacing, boolean space, int tid ){
         this.Tid = tid;
         this.spacing = spacing;
         this.space = space;

     }
     public Track( boolean edittext, int tid,String text, int index){
         this.Tid = tid;
         this.edittext = edittext;
         this.text = text;
         this.index = index;
     }
     public Track(  int tid,boolean imgOpacity,float imgopacity1){
         this.Tid = tid;
         this.imgOpacity = imgOpacity;
         this.imgopacity1 = imgopacity1;
     }
     public Track(ImageView img, int tid,float flipx, boolean flip){
         this.Tid = tid;
         this.imageView = img;
         this.flipx = flipx;
         this.flip = flip;

     }
     public Track(int tid, boolean isunderline, boolean underline){
        this.Tid = tid;
        this.isunderline = isunderline;
        this.underline = underline;

     }
     public Track(int tid, int textAlignment, boolean allign){
         this.Tid = tid;
         this.textAlignment = textAlignment;
         this.allign = allign;

     }
     public Track(int tid,String item, float hueValue){
         this.Tid = tid;
         this.item = item;
         this.hueValue = hueValue;
     }


}
