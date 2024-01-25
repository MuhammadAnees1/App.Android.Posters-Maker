package com.example.postersmaker;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Track {

   String text ;
    float positionX ;


    public boolean isIstextcolor() {
        return istextcolor;
    }

    float positionY ;
    Boolean position = false ;
    int width ;
    int imgwidth ;



    public TextLayout getTextLayout1() {
        return textLayout1;
    }

    int imgheight ;

    public int getImgwidth() {
        return imgwidth;
    }

    public int getImgheight() {
        return imgheight;
    }

    int height ;


    boolean resize = false ;

    float textSize ;
    int textAlignment ;
    float alpha ;
    float shadow ;
    float rotation ;
    boolean rotate = false ;
    float spacing ;
    boolean lock ;
    int textID ;
    float shadowDx ;
    float shadowDy ;
    int font;
    int textColor;
    boolean istextcolor = false ;

    TextLayout textLayout1;
    int Tid;

    public String getText() {
        return text;
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

    public float getAlpha() {
        return alpha;
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

    public int getTextID() {
        return textID;
    }

    public float getShadowDx() {
        return shadowDx;
    }

    public float getShadowDy() {
        return shadowDy;
    }

    public int getFont() {
        return font;
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
    public Boolean getPosition() {
        return position;
    }

    public boolean isRotate() {
        return rotate;
    }
    public boolean isResize() {
        return resize;
    }


    static List<Track> list = new ArrayList<>();

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


}
