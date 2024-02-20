package com.example.postersmaker;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageLayout {
    private FrameLayout frameLayout;
    private RelativeLayout borderLayout;
    private Button deleteButton;
    private Button rotateButton;
    private Button resizeButton;
    private Button saveButton;
     Boolean isLocked;
     boolean isFrame = false;
    private Uri imageUri;
    ImageView imageView;
    Bitmap imgBitmap= null, bitmap = null;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    int id;

    public Uri getImageUri() {
        return imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageLayout(FrameLayout frameLayout, RelativeLayout borderLayout, Button deleteButton, Button rotateButton, Button resizeButton, Button saveButton , Boolean isLocked, Uri imageUri, ImageView imageView, int id, boolean isFrame) {
        this.frameLayout = frameLayout;
        this.borderLayout = borderLayout;
        this.deleteButton = deleteButton;
        this.rotateButton = rotateButton;
        this.resizeButton = resizeButton;
        this.saveButton = saveButton;
        this.isLocked = isLocked;
        this.imageView = imageView;
        this.imageUri = imageUri;
        this.id = id;
        this.isFrame = isFrame;
    }
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
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

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public void setBorderLayout(RelativeLayout borderLayout) {
        this.borderLayout = borderLayout;
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

    private void invalidate() {
        // Implement your custom invalidation logic here
        frameLayout.invalidate();
    }

}